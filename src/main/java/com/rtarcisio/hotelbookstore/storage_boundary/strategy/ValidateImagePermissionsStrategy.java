package com.rtarcisio.hotelbookstore.storage_boundary.strategy;

import com.rtarcisio.hotelbookstore.auth_boundary.domains.AuthUser;
import com.rtarcisio.hotelbookstore.storage_boundary.enums.OwnerType;

public interface ValidateImagePermissionsStrategy {
    void validate(OwnerType ownerType, String ownerId, AuthUser authUser);
}