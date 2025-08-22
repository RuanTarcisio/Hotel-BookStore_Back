package com.rtarcisio.hotelbookstore.shared_boundary.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserOAuthDTO(
        @Schema(description = "ID userAuth", example = "UUID")
        String userAuthId,
        @Schema(description = "Nome completo", example = "Ruan Tarcísio")
        String name,
        @Schema(description = "E-mail do usuário", example = "ruan@example.com")
        String email,
        @Schema(description = "Foto do usuário", example = "")
        String profileImageUrl) {

}
