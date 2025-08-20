package com.rtarcisio.hotelbookstore.storage.enums;

import lombok.Getter;

@Getter
public enum ImageType {
    PROFILE("PROFILE"), COVER("COVER"), GALLERY("GALLERY"), THUMBNAIL("THUMBNAIL");
    private String value;

    ImageType(String value) {
        this.value = value;
    }
}
