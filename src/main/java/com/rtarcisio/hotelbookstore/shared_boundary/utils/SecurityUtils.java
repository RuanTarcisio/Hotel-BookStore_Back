package com.rtarcisio.hotelbookstore.shared_boundary.utils;

import com.rtarcisio.hotelbookstore.auth_boundary.domains.AuthUser;
import com.rtarcisio.hotelbookstore.shared_boundary.exceptions.AuthenticationException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public final class SecurityUtils {

    private SecurityUtils() {
        // Classe utilitária - não deve ser instanciada
    }

    public static Optional<String> getSubject() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 1. Verifica se a autenticação é válida.
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        // 2. Extrai o principal (sujeito) da autenticação.
        Object principal = authentication.getPrincipal();

        // 3. Checa o tipo do principal e retorna o nome de usuário.
        if (principal instanceof AuthUser) {
            return Optional.of(((AuthUser) principal).getAuthUserId());
        }

        if (principal instanceof String) {
            String name = (String) principal;
            if (!name.equals("anonymousUser")) {
                return Optional.of(name);
            }
        }

        return Optional.empty();
    }

    public static String getSubjectOrThrow() {
        return getSubject()
                .orElseThrow(() -> new AuthenticationException("Nenhum subject disponível no contexto de segurança"));
    }

    public static String extractTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("auth_token".equalsIgnoreCase(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
    public static String getBearerToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        throw new AuthenticationException("Token não fornecido");
    }

    public static String getAuthInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return "Não autenticado";
        }

        return String.format("Nome: %s, Roles: %s, Autenticado: %s",
                authentication.getName(),
                authentication.getAuthorities(),
                authentication.isAuthenticated());
    }


}

