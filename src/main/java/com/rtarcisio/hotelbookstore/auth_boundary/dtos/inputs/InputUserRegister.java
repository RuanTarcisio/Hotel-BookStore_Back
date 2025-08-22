package com.rtarcisio.hotelbookstore.auth_boundary.dtos.inputs;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

@Data
public class InputUserRegister{

    @NotBlank
    private String name;
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String matchPassword;
    private String phoneNumber;
    @CPF
    private String cpf;
    @JsonFormat(pattern = "dd/MM/yyyy" )
    private LocalDate birthdate;
//    private MultipartFile profileImage;
}
