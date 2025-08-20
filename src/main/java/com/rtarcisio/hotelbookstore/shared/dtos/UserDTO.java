package com.rtarcisio.hotelbookstore.shared.dtos;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record UserDTO(
        @Schema(description = "Nome completo", example = "Ruan Tarcísio")
        String name,
        @Schema(description = "E-mail do usuário", example = "ruan@example.com")
        String email,
        @Schema(description = "Url da foto", example = ".../v1/users/profile/photo")
        String urlImage, @JsonFormat(pattern = "dd/MM/yyyy" )
        LocalDate birthdate) {

}
