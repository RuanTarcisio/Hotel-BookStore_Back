package com.rtarcisio.hotelbookstore.storage.dtos.inputs;

import com.rtarcisio.hotelbookstore.storage.enums.ImageType;
import com.rtarcisio.hotelbookstore.storage.enums.OwnerType;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

public record UploadContext(
        MediaType contentType,
        ImageType imageType,
        OwnerType ownerType,
        MultipartFile file,
        String ownerId
) {}