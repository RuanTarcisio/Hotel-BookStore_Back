package com.rtarcisio.hotelbookstore.storage.validations;

import com.rtarcisio.hotelbookstore.storage.dtos.inputs.ImageUploadInput;
import com.rtarcisio.hotelbookstore.storage.validations.ImageValidationRules.ValidationRule;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import static com.rtarcisio.hotelbookstore.storage.validations.ImageValidationRules.getRule;

public class FileInputValidator implements ConstraintValidator<FileInput, ImageUploadInput> {

    private boolean required;

    @Override
    public void initialize(FileInput constraint) {
        this.required = constraint.required();
    }

    @Override
    public boolean isValid(ImageUploadInput input, ConstraintValidatorContext context) {
        if (input == null) {
            return !required;
        }
        ValidationRule rule = getRule(
                input.getImageType(),
                input.getOwnerType()
        );

        MultipartFile file = input.getFile();

        if (rule.required() && (file == null || file.isEmpty())) {
            addConstraintViolation(context, "Arquivo é obrigatório", "file");
            return false;
        }

        if (file == null || file.isEmpty()) {
            return true;
        }

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