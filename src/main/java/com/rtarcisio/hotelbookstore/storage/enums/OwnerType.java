package com.rtarcisio.hotelbookstore.storage.enums;

import lombok.Getter;

@Getter
public enum OwnerType{
    USER("USER"), ROOM("ROOM"), PRODUCT("PRODUCT");
    private String value;
    OwnerType(String value){
        this.value = value;
    }
}