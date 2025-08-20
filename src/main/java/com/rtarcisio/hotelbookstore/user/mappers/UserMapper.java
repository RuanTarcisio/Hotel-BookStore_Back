package com.rtarcisio.hotelbookstore.user.mappers;


import com.rtarcisio.hotelbookstore.auth.dtos.inputs.InputUserRegister;
import com.rtarcisio.hotelbookstore.shared.dtos.UserDTO;
import com.rtarcisio.hotelbookstore.shared.events.UserOAuthRegisteredEvent;
import com.rtarcisio.hotelbookstore.shared.events.UserRegisteredEvent;
import com.rtarcisio.hotelbookstore.user.domains.User;
import com.rtarcisio.hotelbookstore.user.dtos.inputs.InputCompletedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class UserMapper {

    public static UserDTO inputUserRegisterToUserDTO(InputUserRegister user) {
        return null;
    }

    public static User eventToUser(UserRegisteredEvent event) {
        User user = new User();
        user.setCpf(event.getCpf());
        user.setEmail(event.getEmail());
        user.setBirthdate(event.getBirthdate());
        user.setAuthUserId(event.getAuthUserId());
        user.setName(event.getName());
        user.setCreatedAt(LocalDateTime.now());
        return user;
    }

    public static User inputUserCompletedtTouser(String authID, String email, InputCompletedUser input) {

        User user = new User();
        user.setCpf(input.cpf());
        user.setEmail(email);
        user.setBirthdate(input.birthdate());
        user.setAuthUserId(authID);
        user.setName(input.name());
        user.setCreatedAt(LocalDateTime.now());
        return user;
    }

    public static User eventToUser(UserOAuthRegisteredEvent event) {
        User user = new User();
        user.setAuthUserId(event.userAuthId());
        user.setName(event.name());
        user.setEmail(event.email());
        user.setProfileImageUrl(event.profileImageUrl());
        user.setCreatedAt(LocalDateTime.now());
        return user;
    }
}
