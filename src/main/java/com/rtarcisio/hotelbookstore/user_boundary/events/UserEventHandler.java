package com.rtarcisio.hotelbookstore.user_boundary.events;

import com.rtarcisio.hotelbookstore.shared_boundary.events.UserOAuthRegisteredEvent;
import com.rtarcisio.hotelbookstore.shared_boundary.events.UserRegisteredEvent;

public interface UserEventHandler {
    void handleUserRegisteredEvent(UserRegisteredEvent event);
    void handleOAuthUserRegisteredEvent(UserOAuthRegisteredEvent event);

    // Outros métodos de eventos relacionados a User podem ser adicionados aqui
    // void handleUserUpdatedEvent(UserUpdatedEvent event);
    // void handleUserDeletedEvent(UserDeletedEvent event);
}