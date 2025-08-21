package com.rtarcisio.hotelbookstore.storage.enums;

import com.rtarcisio.hotelbookstore.storage.strategy.ValidateImagePermissionsStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ImagePermissionsStrategyFactory {

    private final Map<ImageType, ValidateImagePermissionsStrategy> strategies;

    // ✅ Injeção via construtor - Spring gerencia as instâncias
    public ImagePermissionsStrategyFactory(
            @Qualifier("profileImageStrategy") ValidateImagePermissionsStrategy profileStrategy,
            @Qualifier("coverImageStrategy") ValidateImagePermissionsStrategy coverStrategy,
            @Qualifier("galleryImageStrategy") ValidateImagePermissionsStrategy galleryStrategy) {

        this.strategies = Map.of(
                ImageType.PROFILE, profileStrategy,
                ImageType.COVER, coverStrategy,
                ImageType.GALLERY, galleryStrategy
        );
    }

    public ValidateImagePermissionsStrategy getStrategy(ImageType imageType) {
        ValidateImagePermissionsStrategy strategy = strategies.get(imageType);
        if (strategy == null) {
            throw new IllegalArgumentException("Strategy não encontrada para: " + imageType);
        }
        return strategy;
    }
}
