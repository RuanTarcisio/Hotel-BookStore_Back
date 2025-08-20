package com.rtarcisio.hotelbookstore.auth.mappers;


import com.rtarcisio.hotelbookstore.auth.domains.AuthUser;
import com.rtarcisio.hotelbookstore.shared.dtos.UserDTO;
import com.rtarcisio.hotelbookstore.auth.dtos.inputs.InputUserRegister;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AuthUserMapper {

    public static AuthUser inputToAuthUser(InputUserRegister input) {

        AuthUser authUser = new AuthUser();
        authUser.setEmail(input.getEmail());
        authUser.setPassword(input.getPassword());
        authUser.setCreatedAt(LocalDateTime.now());

        return authUser;
    }

    public static UserDTO inputUserRegisterToUserDTO(InputUserRegister user) {


        return null;
    }
}
