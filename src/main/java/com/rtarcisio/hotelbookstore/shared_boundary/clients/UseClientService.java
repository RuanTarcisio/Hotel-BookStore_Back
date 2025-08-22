package com.rtarcisio.hotelbookstore.shared_boundary.clients;

import com.rtarcisio.hotelbookstore.shared_boundary.dtos.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class UseClientService {
    private final WebClient webClient;

    public Mono<UserResponse> getUserByAuthenticatedUser(String bearerToken) {
        // 1. Obter o authId do contexto de segurança
        String authId = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();

        // 2. Fazer a requisição e retornar o Mono
        return webClient.get()
                .uri("/v1/users/profile/auth", authId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                .retrieve()
                .bodyToMono(UserResponse.class); // Retorna o Mono<User> sem bloquear
    }
}
