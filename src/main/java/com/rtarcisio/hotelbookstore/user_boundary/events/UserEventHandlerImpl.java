package com.rtarcisio.hotelbookstore.user_boundary.events;

import com.rtarcisio.hotelbookstore.shared_boundary.events.UserOAuthRegisteredEvent;
import com.rtarcisio.hotelbookstore.shared_boundary.events.UserRegisteredEvent;
import com.rtarcisio.hotelbookstore.user_boundary.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserEventHandlerImpl implements UserEventHandler {

    private final UserService userService;

    @Override
    @EventListener
    @Async
//    @Retryable(value = {DataAccessException.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public void handleUserRegisteredEvent(UserRegisteredEvent event) {

        userService.registerUser(event);
    }

    @Override
    @EventListener
    @Async
    public void handleOAuthUserRegisteredEvent(UserOAuthRegisteredEvent event) {
        userService.registerUser(event);
    }
}
