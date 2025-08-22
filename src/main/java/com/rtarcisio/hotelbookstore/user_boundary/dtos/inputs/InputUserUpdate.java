package com.rtarcisio.hotelbookstore.user_boundary.dtos.inputs;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public record InputUserUpdate(@NotBlank String name, @NotBlank String email,
                              @JsonFormat(pattern = "dd/MM/yyyy" ) LocalDate birthdate, MultipartFile file) {
}
