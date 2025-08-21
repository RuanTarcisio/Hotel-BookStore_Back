package com.rtarcisio.hotelbookstore.storage.validations.images;

import com.rtarcisio.hotelbookstore.auth.domains.AuthUser;
import com.rtarcisio.hotelbookstore.shared.dtos.ImageUploadInput;
import com.rtarcisio.hotelbookstore.shared.exceptions.ValidationException;
import com.rtarcisio.hotelbookstore.storage.enums.ImagePermissionsStrategyFactory;
import com.rtarcisio.hotelbookstore.storage.enums.ImageType;
import com.rtarcisio.hotelbookstore.storage.enums.MediaTypeExtension;
import com.rtarcisio.hotelbookstore.storage.enums.OwnerType;
import com.rtarcisio.hotelbookstore.storage.strategy.ValidateImagePermissionsStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class ImageValidator {

    private final ImagePermissionsStrategyFactory strategyFactory;

    public void validateUpload(ImageUploadInput input, AuthUser authUser) {
        // 1. Validação básica do arquivo
        if (input.getFile() == null || input.getFile().isEmpty()) {
            throw new ValidationException("Arquivo é obrigatório");
        }

        // 2. Conversão segura dos enums
        ImageType imageType = safeConvertToImageType(input.getImageType());
        OwnerType ownerType = safeConvertToOwnerType(input.getOwnerType());

        // 3. Validação de permissões
        validatePermissions(imageType, ownerType, input.getOwnerId(), authUser);

        // 4. Validação de negócio específica
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

        // ✅ Obtém strategy dinamicamente
        ValidateImagePermissionsStrategy strategy = strategyFactory.getStrategy(imageType);

        // ✅ Executa validação específica
        strategy.validate(ownerType, ownerId, authUser);
    }

    private void validateBusinessRules(ImageType imageType, OwnerType ownerType,
                                       MultipartFile file) {
        // Validações específicas por tipo
        MediaTypeExtension mediaTypeExt = MediaTypeExtension.fromMediaType(
                MediaType.valueOf(file.getContentType())
        ).orElseThrow(() -> new ValidationException("Tipo de mídia não suportado"));

        // Ex: GIF só permitido para galeria
        if (mediaTypeExt == MediaTypeExtension.GIF && imageType != ImageType.GALLERY) {
            throw new ValidationException("GIF só é permitido para galeria");
        }
    }
}