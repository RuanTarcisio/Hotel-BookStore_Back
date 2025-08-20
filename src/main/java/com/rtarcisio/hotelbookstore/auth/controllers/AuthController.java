package com.rtarcisio.hotelbookstore.auth.controllers;

import com.rtarcisio.hotelbookstore.auth.domains.AuthUser;
import com.rtarcisio.hotelbookstore.auth.dtos.CredentialsDTO;
import com.rtarcisio.hotelbookstore.auth.dtos.inputs.InputUserRegister;
import com.rtarcisio.hotelbookstore.auth.security.AuthHandlerUtil;
import com.rtarcisio.hotelbookstore.auth.services.AuthUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/v1/auth")
@RestController
@Tag(name = "Autenticação", description = "Endpoints para autenticação de usuários")
public class AuthController {

    private final AuthUserService authService;
    private final AuthHandlerUtil authHandlerUtil;

    @Operation(summary = "Cadastrar novo usuário", description = "Realiza o cadastro de um novo usuário com envio de dados via formulário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "409", description = "Usuário já existe"),
    })
    @PostMapping(value = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> save(
            @Parameter(description = "Dados do usuário para cadastro", required = true)
            @ModelAttribute @Valid InputUserRegister input) {

        AuthUser authUser = authService.registerUser(input);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(authUser.getAuthUserId())
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @Operation(summary = "Checar sessão ativa", description = "Verifica se a sessão do usuário ainda está ativa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sessão válida"),
            @ApiResponse(responseCode = "401", description = "Sessão expirada ou inválida"),
    })
    @GetMapping("/check-session")
    @Cacheable(value = "userSession", key = "#principal.id")
    public ResponseEntity<?> checkSession(
            @Parameter(hidden = true) @AuthenticationPrincipal AuthUser user,
            HttpServletResponse response) {
        if (user == null) {
            response.addHeader("Set-Cookie", authHandlerUtil.expiredSession());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Map<String, Object> object = new HashMap<>();
        object.put("id", user.getAuthUserId());
        object.put("email", user.getEmail());
        object.put("name", user.getEmail());
        object.put("roles", user.getAuthorities());
//        object.put("profileImage", user.getProfileImageUrl());

        return ResponseEntity.ok().body(object);
    }

    @Operation(summary = "Autenticar usuário", description = "Realiza o login do usuário e seta o cookie de sessão")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas"),
    })
    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(
            @RequestBody CredentialsDTO credentials,
            HttpServletResponse response) throws IOException {

        boolean authenticated = authService.authenticate(credentials.email(), credentials.password(), response);

        if (!authenticated) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
        }

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Logout do usuário", description = "Encerra a sessão do usuário e remove o cookie de autenticação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logout realizado com sucesso")
    })
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        var expiredCookie = authHandlerUtil.expiredSession();
        response.addHeader("Set-Cookie", expiredCookie);
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().build();
    }
}
