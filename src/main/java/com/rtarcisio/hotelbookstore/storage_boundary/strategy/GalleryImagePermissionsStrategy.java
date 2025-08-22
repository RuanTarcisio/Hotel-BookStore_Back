package com.rtarcisio.hotelbookstore.storage_boundary.strategy;

import com.rtarcisio.hotelbookstore.auth_boundary.domains.AuthUser;
import com.rtarcisio.hotelbookstore.shared_boundary.exceptions.ValidationException;
import com.rtarcisio.hotelbookstore.storage_boundary.enums.OwnerType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component("galleryImageStrategy")
public class GalleryImagePermissionsStrategy implements ValidateImagePermissionsStrategy {

    @Override
    public void validate(OwnerType ownerType, String ownerId, AuthUser authUser) {
        // Apenas ADMIN pode uploadar para galeria
        if (!authUser.getAuthorities().contains("ROLE_ADMIN")) {
            throw new AccessDeniedException("Apenas administradores podem uploadar para galeria");
        }

        // Validações específicas para galeria
        if (ownerType != OwnerType.ROOM) {
            throw new ValidationException("Galeria só é permitida para rooms");
        }
    }
}