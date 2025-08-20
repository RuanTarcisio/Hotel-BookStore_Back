package com.rtarcisio.hotelbookstore.storage.services;

import com.rtarcisio.hotelbookstore.shared.exceptions.ImageNotFoundException;
import com.rtarcisio.hotelbookstore.storage.domains.Image;
import com.rtarcisio.hotelbookstore.storage.dtos.inputs.ImageUploadInput;
import com.rtarcisio.hotelbookstore.storage.enums.ImageType;
import com.rtarcisio.hotelbookstore.storage.enums.MediaTypeExtension;
import com.rtarcisio.hotelbookstore.storage.enums.OwnerType;
import com.rtarcisio.hotelbookstore.storage.repositories.ImageRepository;
import com.rtarcisio.hotelbookstore.storage.validations.MediaTypeValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
@Transactional
public class ImageService {
    private final ImageRepository imageRepository;
    private final FileStorageService storageService; // ✅ Serviço de storage
    private final MediaTypeValidator mediaTypeValidator; // ✅ Adicione este serviço

    public Image uploadImage(MultipartFile file, OwnerType ownerType,
                             String ownerId, ImageType imageType) {

        MediaType contentType = MediaType.valueOf(file.getContentType());
        mediaTypeValidator.validateForImageType(contentType, imageType);

        String storagePath = storageService.storeFile(file, ownerType, ownerId, imageType);

        MediaTypeExtension mediaTypeExt = MediaTypeExtension.fromMediaType(contentType)
                .orElseThrow(); // Já validado acima, não deveria falhar

        Image image = new Image();
        image.setOwnerType(ownerType);
        image.setOwnerId(ownerId);
        image.setImageType(imageType);
        image.setOriginalFilename(file.getOriginalFilename());
        image.setSize(file.getSize());
        image.setStoragePath(storagePath);
        image.setCreatedAt(LocalDateTime.now());
        image.setExtension(mediaTypeExt); // ✅ Usando MediaTypeExtension diretamente

        return imageRepository.save(image);
    }

    private void validateImageTypeRestrictions(ImageType imageType, MediaTypeExtension mediaTypeExt) {
        // Exemplo: Perfil só aceita imagens
        if (imageType == ImageType.PROFILE && !mediaTypeExt.getMediaType().getType().equals("image")) {
            throw new RuntimeException(
                    "Imagens de perfil devem ser do tipo image, não " + mediaTypeExt.getMediaType().getType()
            );
        }

        // Exemplo: Gallery não aceita PDF
        if (imageType == ImageType.GALLERY && mediaTypeExt == MediaTypeExtension.PDF) {
            throw new RuntimeException("Galeria não aceita documentos PDF");
        }
    }

    public Image uploadImage(@Valid ImageUploadInput input) {

        MediaType contentType = MediaType.valueOf(input.getFile().getContentType());
        ImageType imageType = ImageType.valueOf(input.getImageType());
        OwnerType ownerType = OwnerType.valueOf(input.getOwnerType());
        MultipartFile file = input.getFile();
        String ownerId = input.getOwnerId();
        mediaTypeValidator.validateForImageType(contentType, imageType);

        String storagePath = storageService.storeFile(file, ownerType, ownerId, imageType);

        MediaTypeExtension mediaTypeExt = MediaTypeExtension.fromMediaType(contentType)
                .orElseThrow(); // Já validado acima, não deveria falhar

        Image image = new Image();
        image.setOwnerType(ownerType);
        image.setOwnerId(ownerId);
        image.setImageType(imageType);
        image.setOriginalFilename(file.getOriginalFilename());
        image.setSize(file.getSize());
        image.setStoragePath(storagePath);
        image.setCreatedAt(LocalDateTime.now());
        image.setExtension(mediaTypeExt); // ✅ Usando MediaTypeExtension diretamente

        return imageRepository.save(image);
    }


    public Image getImageMetadata(String imageId) {
        return imageRepository.findById(imageId)
                .orElseThrow(() -> new ImageNotFoundException("Imagem não encontrada: " + imageId));
    }

    public Image getImageMetadata(String ownerType, String ownerId, String imageType) {
        return imageRepository.findByOwnerTypeAndOwnerIdAndImageType(
                OwnerType.valueOf(ownerType),
                ownerId,
                ImageType.valueOf(imageType)
        ).orElseThrow(() -> new ImageNotFoundException(
                "Imagem não encontrada para: " + ownerType + "/" + ownerId + "/" + imageType
        ));
    }
}