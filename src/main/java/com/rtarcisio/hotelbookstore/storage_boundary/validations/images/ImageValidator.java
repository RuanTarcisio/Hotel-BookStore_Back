package com.rtarcisio.hotelbookstore.storage_boundary.validations.images;

import com.rtarcisio.hotelbookstore.auth_boundary.domains.AuthUser;
import com.rtarcisio.hotelbookstore.shared_boundary.dtos.ImageUploadInput;
import com.rtarcisio.hotelbookstore.shared_boundary.exceptions.ValidationException;
import com.rtarcisio.hotelbookstore.storage_boundary.strategy.ImagePermissionsStrategyFactory;
import com.rtarcisio.hotelbookstore.storage_boundary.enums.ImageType;
import com.rtarcisio.hotelbookstore.storage_boundary.enums.MediaTypeExtension;
import com.rtarcisio.hotelbookstore.storage_boundary.enums.OwnerType;
import com.rtarcisio.hotelbookstore.storage_boundary.strategy.ValidateImagePermissionsStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class ImageValidator {

    private final ImagePermissionsStrategyFactory strategyFactory;

    public void validateUpload(ImageUploadInput input, AuthUser authUser) {
        if (input.getFile() == null || input.getFile().isEmpty()) {
            throw new ValidationException("Arquivo é obrigatório");
        }

        ImageType imageType = safeConvertToImageType(input.getImageType());
        OwnerType ownerType = safeConvertToOwnerType(input.getOwnerType());

        validatePermissions(imageType, ownerType, input.getOwnerId(), authUser);

        validateBusinessRules(imageType, ownerType, input.getFile());
    }

    private ImageType safeConvertToImageType(String value) {
        try {
            return ImageType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Tipo de imagem inválido: " + value);
        }
    }

    private OwnerType safeConvertToOwnerType(String value) {
        try {
            return OwnerType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Tipo de owner inválido: " + value);
        }
    }

    private void validatePermissions(ImageType imageType, OwnerType ownerType,
                                     String ownerId, AuthUser authUser) {

        ValidateImagePermissionsStrategy strategy = strategyFactory.getStrategy(imageType);

        strategy.validate(ownerType, ownerId, authUser);
    }

    private void validateBusinessRules(ImageType imageType, OwnerType ownerType,
                                       MultipartFile file) {
        MediaTypeExtension mediaTypeExt = MediaTypeExtension.fromMediaType(
                MediaType.valueOf(file.getContentType())
        ).orElseThrow(() -> new ValidationException("Tipo de mídia não suportado"));

        if (mediaTypeExt == MediaTypeExtension.GIF && imageType != ImageType.GALLERY) {
            throw new ValidationException("GIF só é permitido para galeria");
        }
    }
}