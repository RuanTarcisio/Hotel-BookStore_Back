package com.rtarcisio.hotelbookstore.shared.events;

import com.rtarcisio.hotelbookstore.shared.models.AbstractImage;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class UserRegisteredEvent {
    private String authUserId;
    private String email;
    private String name;
    private String cpf;
    private LocalDate birthdate;
    private MultipartFile photo;
}
