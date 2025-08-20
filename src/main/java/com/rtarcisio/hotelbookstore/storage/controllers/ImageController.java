package com.rtarcisio.hotelbookstore.storage.controllers;

import com.rtarcisio.hotelbookstore.storage.domains.Image;
import com.rtarcisio.hotelbookstore.storage.dtos.inputs.ImageUploadInput;
import com.rtarcisio.hotelbookstore.storage.enums.ImageType;
import com.rtarcisio.hotelbookstore.storage.enums.OwnerType;
import com.rtarcisio.hotelbookstore.storage.services.FileStorageService;
import com.rtarcisio.hotelbookstore.storage.services.ImageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/images")
public class ImageController {
    private final ImageService imageService;
    private final FileStorageService storageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageResponse> uploadImage(  @Valid @ModelAttribute ImageUploadInput input) {

        Image image = imageService.uploadImage(input.getFile(), input.getOwnerType(), input.getOwnerId(), input.getImageType());
        return ResponseEntity.ok(ImageMapper.toResponse(image));
    }

    @GetMapping("/{imageId}/download")
    public ResponseEntity<Resource> downloadImage(@PathVariable String imageId) {
        Image image = imageService.getImageMetadata(imageId);
        Resource file = storageService.loadFile(image.getStoragePath());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getOriginalFilename() + "\"")
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(file);
    }
}