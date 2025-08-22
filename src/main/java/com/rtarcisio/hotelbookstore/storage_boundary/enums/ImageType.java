package com.rtarcisio.hotelbookstore.storage_boundary.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum ImageType {
    PROFILE("PROFILE"),
    COVER("COVER"),
    GALLERY("GALLERY"),
    THUMBNAIL("THUMBNAIL");

    private final String value;

    ImageType(String value) {
        this.value = value;
    }

    public static Optional<ImageType> fromString(String input) {
        if (input == null || input.isBlank()) {
            return Optional.empty();
        }

        String normalized = input.trim().toUpperCase();
        return Arrays.stream(values())
                .filter(type -> type.value.equalsIgnoreCase(normalized))
                .findFirst();
    }
}
