package com.rtarcisio.hotelbookstore.storage.enums;

import com.rtarcisio.hotelbookstore.storage.strategy.CoverImagePermissionsStrategy;
import com.rtarcisio.hotelbookstore.storage.strategy.GalleryImagePermissionsStrategy;
import com.rtarcisio.hotelbookstore.storage.strategy.ProfileImagePermissionsStrategy;
import com.rtarcisio.hotelbookstore.storage.strategy.ValidateImagePermissionsStrategy;

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
