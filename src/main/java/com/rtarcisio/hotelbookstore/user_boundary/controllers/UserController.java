package com.rtarcisio.hotelbookstore.user_boundary.controllers;

import com.rtarcisio.hotelbookstore.auth_boundary.domains.AuthUser;
import com.rtarcisio.hotelbookstore.shared_boundary.dtos.UserDTO;
import com.rtarcisio.hotelbookstore.shared_boundary.dtos.UserResponse;
import com.rtarcisio.hotelbookstore.shared_boundary.utils.SecurityUtils;
import com.rtarcisio.hotelbookstore.user_boundary.dtos.inputs.InputCompletedUser;
import com.rtarcisio.hotelbookstore.user_boundary.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários e seus perfis")
@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Obter perfil do usuário", description = "Retorna os dados públicos do perfil de um usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Perfil encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/profile/{id}")
//    @CacheControl(maxAge = 30, maxAgeUnit = TimeUnit.SECONDS)
    public ResponseEntity<UserDTO> getProfile(
            @Parameter(description = "ID do usuário", required = true, example = "1")
            @PathVariable String id) {
        return ResponseEntity.ok().body(userService.getUserDTO(id));
    }

    @Operation(summary = "Obter perfil do usuário pela autenticacao", description = "Retorna os dados públicos do perfil de um usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Perfil encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/profile/auth")
    public ResponseEntity<UserResponse> getProfileByAuth(
            @AuthenticationPrincipal AuthUser user) {
        return ResponseEntity.ok().body(userService.getUserResponseByAuthUserId(user.getAuthUserId()));
    }


    @PostMapping("/complete-registration")
    public ResponseEntity<?> completeRegistration(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody InputCompletedUser input) {

        userService.completeRegistration(authUser, input);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()") // ✅ Autenticação básica
    public ResponseEntity<?/*ImageResponse*/> uploadImage(@RequestPart MultipartFile file, HttpServletRequest request) {

        String token = SecurityUtils.extractTokenFromCookie(request);
        if (token == null) {
            throw new AccessDeniedException("Usuario não autenticado.");
        }

        userService.uploadUserProfileImage(file, token);
        return ResponseEntity.ok().build();
    }

//    @Operation(summary = "Atualizar perfil do usuário", description = "Atualiza os dados de perfil de um usuário")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "Perfil atualizado com sucesso"),
//            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
//            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
//    })
//    @PutMapping("/profile/{id}")
//    public ResponseEntity<UserDTO> updateProfile(
//            @Parameter(description = "ID do usuário", required = true, example = "1")
//            @PathVariable Long id,
//
//            @Parameter(description = "Dados para atualização do usuário", required = true)
//            @Valid @RequestBody InputUserUpdate inputUserUpdate) {
//
//        return ResponseEntity.ok().body(userService.updateUser(id, inputUserUpdate));
//    }


//    @Operation(summary = "Buscar foto de perfil", description = "Retorna a imagem de perfil do usuário em formato binário")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "Foto encontrada"),
//            @ApiResponse(responseCode = "404", description = "Usuário ou imagem não encontrados"),
//            @ApiResponse(responseCode = "500", description = "Erro ao processar a imagem")
//    })
//    @GetMapping(value = "/profile/{id}/photo")
//    @Transactional
//    public ResponseEntity<byte[]> find(
//            @Parameter(description = "ID do usuário", required = true, example = "1")
//            @PathVariable String id) {
//
//        var image = userService.getImageByUserId(id);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(image.getExtension().getMediaType());
//        headers.setContentLength(image.getSize());
//        headers.setContentDisposition(ContentDisposition.inline().build());
//        headers.setCacheControl(CacheControl.maxAge(30, TimeUnit.DAYS));
//
//        return new ResponseEntity<>(image.getFile(), headers, HttpStatus.OK);
//    }


}
