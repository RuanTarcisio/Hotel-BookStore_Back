package com.rtarcisio.hotelbookstore.shared.events;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

public record UserOAuthRegisteredEvent (
        @Schema(description = "ID userAuth", example = "UUID")
        String userAuthId,
        @Schema(description = "Nome completo", example = "Ruan Tarcísio")
        String name,
        @Schema(description = "E-mail do usuário", example = "ruan@example.com")
        String email,
        @Schema(description = "Foto do usuário", example = "")
        String profileImageUrl) {

}
