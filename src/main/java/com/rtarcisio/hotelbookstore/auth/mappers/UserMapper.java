package com.rtarcisio.hotelbookstore.auth.mappers;


import com.rtarcisio.hotelbookstore.auth.domains.AuthUser;
import com.rtarcisio.hotelbookstore.shared.dtos.UserDTO;
import com.rtarcisio.hotelbookstore.auth.dtos.inputs.InputUserRegister;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    public static AuthUser inputToAuthUser(InputUserRegister input) {

        AuthUser authUser = new AuthUser();
        authUser.setCpf(input.getCpf());
        authUser.setEmail(input.getEmail());
        authUser.setPassword(input.getPassword());
        authUser.setFullyRegistered(false);

        return authUser;
    }

    public static UserDTO inputUserRegisterToUserDTO(InputUserRegister user) {


        return null;
    }
}
