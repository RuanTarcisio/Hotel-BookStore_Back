package com.rtarcisio.hotelbookstore.shared_boundary.events;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class UserRegisteredEvent {
    private String authUserId;
    private String email;
    private String name;
    private String cpf;
    private LocalDate birthdate;
//    private MultipartFile photo;
}
