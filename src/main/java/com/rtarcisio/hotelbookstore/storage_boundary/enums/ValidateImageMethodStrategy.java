package com.rtarcisio.hotelbookstore.storage_boundary.enums;

import com.rtarcisio.hotelbookstore.storage_boundary.strategy.CoverImagePermissionsStrategy;
import com.rtarcisio.hotelbookstore.storage_boundary.strategy.GalleryImagePermissionsStrategy;
import com.rtarcisio.hotelbookstore.storage_boundary.strategy.ProfileImagePermissionsStrategy;
import com.rtarcisio.hotelbookstore.storage_boundary.strategy.ValidateImagePermissionsStrategy;

public enum ValidateImageMethodStrategy {
    GALLERY(new GalleryImagePermissionsStrategy()),
    COVER(new CoverImagePermissionsStrategy()),
    PROFILE(new ProfileImagePermissionsStrategy());

    private final ValidateImagePermissionsStrategy strategy;

    ValidateImageMethodStrategy(ValidateImagePermissionsStrategy strategy) {
        this.strategy = strategy;
    }

    public ValidateImagePermissionsStrategy getStrategy() {
        return strategy;
    }
}
