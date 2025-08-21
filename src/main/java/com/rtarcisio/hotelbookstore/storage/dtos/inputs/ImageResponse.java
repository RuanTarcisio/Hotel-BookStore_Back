package com.rtarcisio.hotelbookstore.storage.dtos.inputs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageResponse{
        private String id;
        private String ownerType;
        private String ownerId;
        private String imageType;
        private String extension;
        private String storagePath;
        private String urlImage ;
}
