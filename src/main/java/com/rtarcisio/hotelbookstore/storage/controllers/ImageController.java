package com.rtarcisio.hotelbookstore.storage.controllers;

import com.rtarcisio.hotelbookstore.auth.domains.AuthUser;
import com.rtarcisio.hotelbookstore.shared.dtos.ImageUploadInput;
import com.rtarcisio.hotelbookstore.storage.domains.Image;
import com.rtarcisio.hotelbookstore.storage.dtos.inputs.ImageResponse;
import com.rtarcisio.hotelbookstore.storage.mappers.ImageMapper;
import com.rtarcisio.hotelbookstore.storage.services.FileStorageService;
import com.rtarcisio.hotelbookstore.storage.services.ImageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/images")
public class ImageController {
    private final ImageService imageService;
    private final FileStorageService storageService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ImageResponse> uploadImage(
            @Valid @ModelAttribute ImageUploadInput input, @AuthenticationPrincipal AuthUser user) {

        // ✅ Validação específica no service
        Image image = imageService.uploadImage(input, user);
        return ResponseEntity.ok(ImageMapper.toResponse(image));
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadImageByParams(
            @RequestParam String ownerType,
            @RequestParam String ownerId,
            @RequestParam String imageType) {

        Image image = imageService.getImageMetadata(ownerType, ownerId, imageType);
        Resource fileResource = storageService.loadFile(image.getStoragePath());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + image.getOriginalFilename() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, image.getExtension().getMediaType().toString())
                .body(fileResource);
    }

//    @GetMapping("/{imageId}/download")
//    public ResponseEntity<Resource> downloadImage(@PathVariable String imageId) {
//        Image image = imageService.getImageMetadata(imageId);
//        Resource file = storageService.loadFile(image.getStoragePath());
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getOriginalFilename() + "\"")
//                .contentType(MediaType.parseMediaType("application/octet-stream"))
//                .body(file);
//    }
}