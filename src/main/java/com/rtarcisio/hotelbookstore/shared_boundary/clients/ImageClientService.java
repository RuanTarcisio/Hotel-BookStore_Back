package com.rtarcisio.hotelbookstore.shared_boundary.clients;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rtarcisio.hotelbookstore.shared_boundary.exceptions.ResourceForbiddenException;
import com.rtarcisio.hotelbookstore.shared_boundary.exceptions.ValidationException;
import com.rtarcisio.hotelbookstore.storage_boundary.dtos.inputs.ImageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.AbstractResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

    public ImageResponse uploadImage(MultipartFile file, String ownerType, String ownerId, String imageType, String authToken) {

        try {
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new MultipartFileResource(file));
            body.add("ownerType", ownerType);
            body.add("ownerId", ownerId);
            body.add("imageType", imageType);

            return webClient.post().uri("/v1/images/upload").header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken).contentType(MediaType.MULTIPART_FORM_DATA).body(BodyInserters.fromMultipartData(body)).retrieve().onStatus(status -> status.is4xxClientError(), response -> {
                return response.bodyToMono(String.class).flatMap(errorBody -> {
                    String specificMessage = extractSpecificMessage(errorBody);
                    return Mono.error(new HttpClientErrorException(response.statusCode(), specificMessage // ✅ Mensagem específica!
                    ));
                });
            }).onStatus(status -> status.is5xxServerError(), response -> {
                return Mono.error(new HttpServerErrorException(response.statusCode(), "Erro no servidor de imagem"));
            }).bodyToMono(ImageResponse.class).block(Duration.ofSeconds(30));

        } catch (HttpClientErrorException e) {
            log.error("Erro 4xx no upload: {}", e.getMessage());
            if (e.getStatusCode() == HttpStatus.FORBIDDEN) {
                throw new ResourceForbiddenException(e.getMessage());
            } else if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new ValidationException(e.getMessage());
            }
            throw new ResourceForbiddenException(e.getMessage());
        } catch (HttpServerErrorException e) {
            log.error("Erro 5xx no upload: {}", e.getMessage());
            throw new RuntimeException("Servidor indisponível: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Erro inesperado no upload: {}", e.getMessage());
            throw new RuntimeException("Erro inesperado: " + e.getMessage(), e);
        }
    }

    private String extractSpecificMessage(String errorBody) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(errorBody);

            if (jsonNode.has("message")) {
                return jsonNode.get("message").asText();
            }
            if (jsonNode.has("error")) {
                return jsonNode.get("error").asText();
            }
            if (jsonNode.has("detail")) {
                return jsonNode.get("detail").asText();
            }
            return errorBody;

        } catch (Exception e) {
            return errorBody;
        }
    }

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
