package com.rtarcisio.hotelbookstore.storage_boundary.services;

import com.rtarcisio.hotelbookstore.shared_boundary.exceptions.StorageException;
import com.rtarcisio.hotelbookstore.storage_boundary.dtos.inputs.UploadContext;
import com.rtarcisio.hotelbookstore.storage_boundary.enums.ImageType;
import com.rtarcisio.hotelbookstore.storage_boundary.enums.OwnerType;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class FileStorageService {
    public String storeFile(MultipartFile file, OwnerType ownerType,
                            String ownerId, ImageType imageType) {

        String filename = generateUniqueFilename(file.getOriginalFilename());
        String storagePath = String.format("%s/%s/%s/%s",
                ownerType.name().toLowerCase(),
                ownerId,
                imageType.name().toLowerCase(),
                filename
        );

        Path destination = Paths.get("uploads", storagePath);
        try {
            Files.createDirectories(destination.getParent());
            Files.copy(file.getInputStream(), destination);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return storagePath;
    }

    public String storeFile(UploadContext context) {
        String filename = generateUniqueFilename(context.file().getOriginalFilename());
        String storagePath = String.format("%s/%s/%s/%s",
                context.ownerType().name().toLowerCase(),
                context.ownerId(),
                context.imageType().name().toLowerCase(),
                filename
        );

        Path destination = Paths.get("uploads", storagePath);
        try {
            Files.createDirectories(destination.getParent());
            Files.copy(context.file().getInputStream(), destination);
            return storagePath;
        } catch (IOException e) {
            throw new StorageException("Falha ao salvar arquivo: " + e.getMessage(), e);
        }
    }

    public Resource loadFile(String storagePath) {
        Path filePath = Paths.get("uploads", storagePath);
        return new FileSystemResource(filePath.toFile());
    }

    private String generateUniqueFilename(String originalFilename) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        return timestamp + "_" + originalFilename;
    }
}
