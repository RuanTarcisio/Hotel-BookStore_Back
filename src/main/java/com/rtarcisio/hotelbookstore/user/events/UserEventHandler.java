package com.rtarcisio.hotelbookstore.user.events;

import com.rtarcisio.hotelbookstore.shared.events.UserOAuthRegisteredEvent;
import com.rtarcisio.hotelbookstore.shared.events.UserRegisteredEvent;

public interface UserEventHandler {
    void handleUserRegisteredEvent(UserRegisteredEvent event);
    void handleOAuthUserRegisteredEvent(UserOAuthRegisteredEvent event);

    // Outros métodos de eventos relacionados a User podem ser adicionados aqui
    // void handleUserUpdatedEvent(UserUpdatedEvent event);
    // void handleUserDeletedEvent(UserDeletedEvent event);
}