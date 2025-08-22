package com.rtarcisio.hotelbookstore.user_boundary.dtos.inputs;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public record InputCompletedUser(String name, String cpf, String password, String matchPassword, String phone, @JsonFormat(pattern = "dd/MM/yyyy" ) LocalDate birthdate, MultipartFile photo) {
}
