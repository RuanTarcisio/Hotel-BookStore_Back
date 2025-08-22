package com.rtarcisio.hotelbookstore.storage_boundary.dtos.inputs;

import com.rtarcisio.hotelbookstore.storage_boundary.enums.ImageType;
import com.rtarcisio.hotelbookstore.storage_boundary.enums.OwnerType;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

public record UploadContext(
        MediaType contentType,
        ImageType imageType,
        OwnerType ownerType,
        MultipartFile file,
        String uploaderId,
        String ownerId
) {}