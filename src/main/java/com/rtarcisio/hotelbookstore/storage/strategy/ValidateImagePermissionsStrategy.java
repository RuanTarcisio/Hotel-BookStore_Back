package com.rtarcisio.hotelbookstore.storage.strategy;

import com.rtarcisio.hotelbookstore.auth.domains.AuthUser;
import com.rtarcisio.hotelbookstore.storage.enums.OwnerType;

public interface ValidateImagePermissionsStrategy {
    void validate(OwnerType ownerType, String ownerId, AuthUser authUser);
}