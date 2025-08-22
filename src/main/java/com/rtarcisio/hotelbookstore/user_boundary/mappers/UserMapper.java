package com.rtarcisio.hotelbookstore.user_boundary.mappers;


import com.rtarcisio.hotelbookstore.auth_boundary.dtos.inputs.InputUserRegister;
import com.rtarcisio.hotelbookstore.shared_boundary.dtos.UserDTO;
import com.rtarcisio.hotelbookstore.shared_boundary.events.UserOAuthRegisteredEvent;
import com.rtarcisio.hotelbookstore.shared_boundary.events.UserRegisteredEvent;
import com.rtarcisio.hotelbookstore.shared_boundary.dtos.UserResponse;
import com.rtarcisio.hotelbookstore.user_boundary.domains.User;
import com.rtarcisio.hotelbookstore.user_boundary.dtos.inputs.InputCompletedUser;
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

    public static UserResponse userToUserResponse(User user) {
        return new UserResponse(user.getUserId(), user.getName(), user.getEmail(), user.getProfileImageUrl(), user.getBirthdate());
    }
}
