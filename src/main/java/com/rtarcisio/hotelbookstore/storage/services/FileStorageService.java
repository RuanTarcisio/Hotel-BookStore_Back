package com.rtarcisio.hotelbookstore.storage.services;

import com.rtarcisio.hotelbookstore.storage.enums.ImageType;
import com.rtarcisio.hotelbookstore.storage.enums.OwnerType;
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

        // Gera path organizado
        String filename = generateUniqueFilename(file.getOriginalFilename());
        String storagePath = String.format("%s/%s/%s/%s",
                ownerType.name().toLowerCase(),
                ownerId,
                imageType.name().toLowerCase(),
                filename
        );

        // Salva no filesystem (depois migra para S3)
        Path destination = Paths.get("uploads", storagePath);
        try {
            Files.createDirectories(destination.getParent());
            Files.copy(file.getInputStream(), destination);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return storagePath; // Retorna apenas o caminho
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
