package com.rtarcisio.hotelbookstore.storage.dtos.inputs;

public record ImageResponse(String id, String ownerType, String ownerId, String imageType, String extension, String storagePath) {
}
