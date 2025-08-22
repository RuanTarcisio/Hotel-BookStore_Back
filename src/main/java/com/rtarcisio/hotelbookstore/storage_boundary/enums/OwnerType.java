package com.rtarcisio.hotelbookstore.storage_boundary.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum OwnerType {
    USER("USER"),
    ROOM("ROOM"),
    PRODUCT("PRODUCT");

    private final String value;

    OwnerType(String value) {
        this.value = value;
    }

    public static Optional<OwnerType> fromString(String input) {
        if (input == null || input.isBlank()) {
            return Optional.empty();
        }

        String normalized = input.trim().toUpperCase();
        return Arrays.stream(values())
                .filter(type -> type.value.equalsIgnoreCase(normalized))
                .findFirst();
    }

    public static OwnerType fromStringOrThrow(String input) {
        return fromString(input)
                .orElseThrow(() -> new IllegalArgumentException("OwnerType inválido: " + input));
    }
}
