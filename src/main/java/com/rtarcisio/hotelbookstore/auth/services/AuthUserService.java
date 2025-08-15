package com.rtarcisio.hotelbookstore.auth.services;

import com.rtarcisio.hotelbookstore.auth.domains.AuthUser;
import com.rtarcisio.hotelbookstore.auth.domains.UserConnectedAccount;
import com.rtarcisio.hotelbookstore.auth.dtos.inputs.InputUserRegister;
import com.rtarcisio.hotelbookstore.auth.repositories.AuthUserRepository;
import com.rtarcisio.hotelbookstore.auth.mappers.UserMapper;
import com.rtarcisio.hotelbookstore.auth.repositories.ConnectedAccountRepository;
import com.rtarcisio.hotelbookstore.auth.security.AuthHandlerUtil;
import com.rtarcisio.hotelbookstore.shared.dtos.UserDTO;
import com.rtarcisio.hotelbookstore.shared.dtos.UserOAuthDTO;
import com.rtarcisio.hotelbookstore.shared.exceptions.UsuarioNaoEncontradoException;
import com.rtarcisio.hotelbookstore.user.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthUserService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthUserRepository authUserRepository;
    private final ConnectedAccountRepository connectedAccountRepository;
    private final AuthHandlerUtil authHandlerUtil;

    public boolean authenticate(String email, String password, HttpServletResponse response) throws IOException {

        var user = getByEmail(email);

        if (!user.isEnabled()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Conta não ativada. Verifique seu e-mail.");
            return false;
        }

        if (passwordEncoder.matches(password, user.getPassword())) {
            authHandlerUtil.authenticateUser(user, false, response);
            return true;
        }
        return false;
    }

    private AuthUser getByEmail(String email) {
        return authUserRepository.findByEmail(email).orElseThrow(() ->  new UsuarioNaoEncontradoException("Usuario não encontrado."));
    }

    public AuthUser registerUser(InputUserRegister input) {

        AuthUser authUser = UserMapper.inputToAuthUser(input);
        authUser = authUserRepository.save(authUser);
        
        UserDTO dto = UserMapper.inputUserRegisterToUserDTO(input);
        userService.registerUser(dto);
        return authUser;
    }

    public  AuthUser createUserFromOauth2User(OAuth2AuthenticationToken authentication, String profileImageUrl) {
        AuthUser authUser = new AuthUser(authentication.getPrincipal());
        String email = authentication.getPrincipal().getAttribute("email");
        String name = authentication.getPrincipal().getAttribute("name");String provider = authentication.getAuthorizedClientRegistrationId();
        String providerId = authentication.getName();
        UserConnectedAccount connectedAccount = new UserConnectedAccount(provider, providerId, authUser);
        authUser.setEmail(email);
        authUser.addConnectedAccount(connectedAccount);
        authUser = authUserRepository.save(authUser);
        UserOAuthDTO userOAuthDTO = new UserOAuthDTO(authUser.getUserId(), name, profileImageUrl);
        userService.registerUser(userOAuthDTO);
        connectedAccountRepository.save(connectedAccount);
        return authUser;
    }
}
