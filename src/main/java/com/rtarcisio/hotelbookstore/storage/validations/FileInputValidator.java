package com.rtarcisio.hotelbookstore.storage.validations;

import com.rtarcisio.hotelbookstore.shared.dtos.ImageUploadInput;
import com.rtarcisio.hotelbookstore.storage.enums.ImageType;
import com.rtarcisio.hotelbookstore.storage.enums.OwnerType;
import com.rtarcisio.hotelbookstore.storage.validations.images.ImageValidationRules.ValidationRule;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static com.rtarcisio.hotelbookstore.storage.validations.images.ImageValidationRules.getRule;

public class FileInputValidator implements ConstraintValidator<FileInput, ImageUploadInput> {

    private boolean hasError;
    private boolean required;

    @Override
    public boolean isValid(ImageUploadInput input, ConstraintValidatorContext context) {
        if (input == null) {
            return !required;
        }
        Optional<ImageType> imageType = ImageType.fromString(input.getImageType());
        Optional<OwnerType> ownerType = OwnerType.fromString(input.getOwnerType());
        MultipartFile file = input.getFile();

        if (imageType.isEmpty()) {
            addConstraintViolation(context, "ImageType invalido", "imageType");
            hasError = true;
        }
        if (ownerType.isEmpty()) {
            addConstraintViolation(context, "OwnerType invalido", "ownerType");
            hasError = true;
        }
        if ((file == null || file.isEmpty())) {
            addConstraintViolation(context, "Arquivo é obrigatório", "file");
            hasError = true;
        }
        if(hasError){
            return false;
        }
        // ✅ Obtém regras baseadas no contexto
        ValidationRule rule = getRule(
                imageType.get(),
                ownerType.get()
        );

        // Validação de content type
        if (!rule.allowedContentTypes().contains(file.getContentType())) {
            addConstraintViolation(context,
                    String.format("Tipo de arquivo não permitido para %s/%s. Permitidos: %s",
                            input.getImageType(), input.getOwnerType(), rule.allowedContentTypes()),
                    "file");
            return false;
        }

        // Validação de tamanho
        if (file.getSize() > rule.maxSize().toBytes()) {
            addConstraintViolation(context,
                    String.format("Tamanho máximo excedido para %s/%s. Máximo: %s",
                            input.getImageType(), input.getOwnerType(), rule.maxSize()),
                    "file");
            return false;
        }

        if (file.getSize() < rule.minSize().toBytes()) {
            addConstraintViolation(context,
                    String.format("Arquivo muito pequeno para %s/%s. Mínimo: %s",
                            input.getImageType(), input.getOwnerType(), rule.minSize()),
                    "file");
            return false;
        }

        return true;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String message, String field) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode(field)
                .addConstraintViolation();
    }
}