package com.rtarcisio.hotelbookstore.storage.strategy;

import com.rtarcisio.hotelbookstore.auth.domains.AuthUser;
import com.rtarcisio.hotelbookstore.shared.exceptions.ValidationException;
import com.rtarcisio.hotelbookstore.storage.enums.OwnerType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component("profileImageStrategy") // ✅ Nome único para cada strategy
public class ProfileImagePermissionsStrategy implements ValidateImagePermissionsStrategy {

    @Override
    public void validate(OwnerType ownerType, String ownerId, AuthUser authUser) {
        // Apenas para usuários
        if (ownerType != OwnerType.USER) {
            throw new ValidationException("Imagem de perfil só é permitida para users");
        }

        // User só pode uploadar para si mesmo
        if (!authUser.getAuthorities().contains("USER") && !ownerId.equals(authUser.getAuthUserId())) {
            throw new AccessDeniedException("Não é permitido upload de perfil para outro usuário");
        }

        // Admin pode uploadar para qualquer user
        if (!authUser.getAuthorities().contains("ADMIN")) {
            // Admin tem permissão total
            return;
        }
    }
}