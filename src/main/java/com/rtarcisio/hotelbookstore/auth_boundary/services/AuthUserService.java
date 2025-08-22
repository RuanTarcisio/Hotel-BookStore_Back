package com.rtarcisio.hotelbookstore.auth_boundary.services;

import com.rtarcisio.hotelbookstore.auth_boundary.domains.AuthUser;
import com.rtarcisio.hotelbookstore.auth_boundary.dtos.inputs.InputUserRegister;
import com.rtarcisio.hotelbookstore.auth_boundary.mappers.AuthUserMapper;
import com.rtarcisio.hotelbookstore.auth_boundary.repositories.AuthUserRepository;
import com.rtarcisio.hotelbookstore.auth_boundary.security.AuthHandlerUtil;
import com.rtarcisio.hotelbookstore.shared_boundary.events.UserRegisteredEvent;
import com.rtarcisio.hotelbookstore.shared_boundary.exceptions.UsuarioNaoEncontradoException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthUserService {

    private final PasswordEncoder passwordEncoder;
    private final AuthUserRepository authUserRepository;
    private final AuthHandlerUtil authHandlerUtil;
    private final ApplicationEventPublisher eventPublisher;


    public boolean authenticate(String email, String password, HttpServletResponse response) throws IOException {

        var user = getByEmail(email);

//        if (!user.isEnabled()) {
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Conta não ativada. Verifique seu e-mail.");
//            return false;
//        }

        if (passwordEncoder.matches(password, user.getPassword())) {
            authHandlerUtil.authenticateUser(user, false, response);
            return true;
        }
        return false;
    }

    private AuthUser getByEmail(String email) {
        return authUserRepository.findByEmail(email).orElseThrow(() ->  new UsuarioNaoEncontradoException("Usuario não encontrado."));
    }

    @Transactional
    public AuthUser registerUser(InputUserRegister input) {

        AuthUser authUser = AuthUserMapper.inputToAuthUser(input);
        authUser.setPassword(passwordEncoder.encode(input.getPassword()));
        authUser = authUserRepository.save(authUser);

        eventPublisher.publishEvent(new UserRegisteredEvent(
                authUser.getAuthUserId(),
                authUser.getEmail(),
                input.getName(),
                input.getCpf(),
                input.getBirthdate()));
//                input.getProfileImage()));


        return authUser;
//        UserDTO dto = UserMapper.inputUserRegisterToUserDTO(input);
//        userService.registerUser(dto);
//        return authUser;
    }

    public void makeFullyRegistred(AuthUser authUser){
        authUser.markAsFullyRegistered();
//        authUser.setEnabled(true);
        authUserRepository.save(authUser);
    }


    public void makeFullyRegistred(String authUserId) {
        AuthUser authUser = authUserRepository.findById(authUserId).get();
        authUser.markAsFullyRegistered();
//        authUser.setEnabled(true);
        authUserRepository.save(authUser);
    }
}
