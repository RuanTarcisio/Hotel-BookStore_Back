package com.rtarcisio.hotelbookstore.shared.clients;

import com.rtarcisio.hotelbookstore.storage.dtos.inputs.ImageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.AbstractResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageClientService {

    private final WebClient webClient;

    public ImageResponse uploadImage(MultipartFile file, String ownerType,
                                     String ownerId, String imageType, String authToken) {

        try {
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new MultipartFileResource(file));
            body.add("ownerType", ownerType);
            body.add("ownerId", ownerId);
            body.add("imageType", imageType);
            return webClient.post()
                    .uri("/v1/images/upload")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData(body))
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError(), response -> {
                        return response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new HttpClientErrorException(
                                        response.statusCode(),
                                        "Erro no upload: " + errorBody
                                )));
                    })
                    .onStatus(status -> status.is5xxServerError(), response -> {
                        return Mono.error(new HttpServerErrorException(
                                response.statusCode(),
                                "Erro no servidor de imagem"
                        ));
                    })
                    .bodyToMono(ImageResponse.class)
                    .block(Duration.ofSeconds(30)); // Timeout de 30 segundos

        } catch (HttpClientErrorException e) {
            log.error("Erro 4xx no upload: {}", e.getMessage());
            throw new RuntimeException("Erro de validação: " + e.getMessage(), e);
        } catch (HttpServerErrorException e) {
            log.error("Erro 5xx no upload: {}", e.getMessage());
            throw new RuntimeException("Servidor indisponível: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Erro inesperado no upload: {}", e.getMessage());
            throw new RuntimeException("Erro inesperado: " + e.getMessage(), e);
        }
    }

    // Classe auxiliar para converter MultipartFile
    private static class MultipartFileResource extends AbstractResource {
        private final MultipartFile file;

        public MultipartFileResource(MultipartFile file) {
            this.file = file;
        }

        @Override
        public String getFilename() {
            return file.getOriginalFilename();
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return file.getInputStream();
        }

        @Override
        public long contentLength() {
            return file.getSize();
        }

        @Override
        public String getDescription() {
            return "MultipartFile resource: " + file.getOriginalFilename();
        }
    }
}
