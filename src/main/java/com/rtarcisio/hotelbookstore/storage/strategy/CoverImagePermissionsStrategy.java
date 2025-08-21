package com.rtarcisio.hotelbookstore.storage.strategy;

import com.rtarcisio.hotelbookstore.auth.domains.AuthUser;
import com.rtarcisio.hotelbookstore.storage.enums.OwnerType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component("coverImageStrategy")
public class CoverImagePermissionsStrategy implements ValidateImagePermissionsStrategy {

    @Override
    public void validate(OwnerType ownerType, String ownerId, AuthUser authUser) {
        // USER ou ADMIN
        if (!authUser.getAuthorities().contains("ROLE_ADMIN")&& !authUser.getAuthorities().contains("ROLE_USER"))  {
            throw new AccessDeniedException("Acesso negado para upload de capa");
        }

        // Se for USER, deve ser o próprio
        if (ownerType == OwnerType.USER && authUser.getAuthorities().contains("ROLE_USER")) {
            if (!ownerId.equals(authUser.getAuthUserId())) {
                throw new AccessDeniedException("Não é permitido upload de capa para outro usuário");
            }
        }
    }
}