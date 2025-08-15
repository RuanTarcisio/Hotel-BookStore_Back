package com.rtarcisio.hotelbookstore.auth.security;

import com.rtarcisio.hotelbookstore.auth.domains.AuthUser;
import com.rtarcisio.hotelbookstore.auth.repositories.AuthUserRepository;
import com.rtarcisio.hotelbookstore.auth.repositories.ConnectedAccountRepository;
import com.rtarcisio.hotelbookstore.auth.domains.UserConnectedAccount;
import com.rtarcisio.hotelbookstore.auth.services.AuthUserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class Oauth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final ConnectedAccountRepository connectedAccountRepository;
    private final AuthUserRepository authUserRepository;
    private final AuthHandlerUtil authHandlerUtil;
    private final AuthUserService authUserService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        try {
            OAuth2AuthenticationToken authenticationToken = (OAuth2AuthenticationToken) authentication;
            String provider = authenticationToken.getAuthorizedClientRegistrationId();
            String providerId = authenticationToken.getName();
            String email = authenticationToken.getPrincipal().getAttribute("email");
            String profileImageUrl = null;

            if ("google".equals(provider)) {
                profileImageUrl = (String) authenticationToken.getPrincipal().getAttribute("picture");
            } else if ("github".equals(provider)) {
                profileImageUrl = (String) authenticationToken.getPrincipal().getAttribute("avatar_url");
            }

            log.info("OAuth2 Login Success - Provider: {}, ProviderId: {}, Email: {}", provider, providerId, email);

            Optional<UserConnectedAccount> connectedAccount = connectedAccountRepository.findByProviderAndProviderId(provider, providerId);
            if (connectedAccount.isPresent()) {
                log.info("User already connected - UserId: {}", connectedAccount.get().getAuthUser().getUserId());
                authHandlerUtil.authenticateUser(connectedAccount.get().getAuthUser(), true, response);
                return;
            }

            AuthUser existingUser = authUserRepository.findByEmail(email).orElse(null);
            if (existingUser != null) {
                log.info("Existing authUser found - UserId: {}", existingUser.getUserId());
                UserConnectedAccount newConnectedAccount = new UserConnectedAccount(provider, providerId, existingUser);
                existingUser.addConnectedAccount(newConnectedAccount);
                existingUser = authUserRepository.save(existingUser);
                connectedAccountRepository.save(newConnectedAccount);
                authHandlerUtil.authenticateUser(existingUser, true, response);
            } else {
                log.info("Creating new authUser from OAuth2");
                AuthUser newUser = authUserService.createUserFromOauth2User(authenticationToken, profileImageUrl);
                authHandlerUtil.authenticateUser(newUser, true, response);
            }
        } catch (Exception e) {
            log.error("Error during OAuth2 login success handling", e);
            response.sendRedirect("/login?error=true");
        }
    }


}