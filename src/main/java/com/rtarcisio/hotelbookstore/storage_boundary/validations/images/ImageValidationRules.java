package com.rtarcisio.hotelbookstore.storage_boundary.validations.images;

import com.rtarcisio.hotelbookstore.storage_boundary.enums.ImageType;
import com.rtarcisio.hotelbookstore.storage_boundary.enums.OwnerType;
import org.springframework.util.unit.DataSize;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ImageValidationRules {

    public record ValidationRule(
            List<String> allowedContentTypes,
            DataSize maxSize,
            DataSize minSize,
            boolean required
    ) {}

    // ✅ Regras específicas por tipo de imagem
    private static final Map<ImageType, ValidationRule> IMAGE_TYPE_RULES = Map.of(
            ImageType.PROFILE, new ValidationRule(
                    List.of("image/jpeg", "image/png"), // Apenas JPEG/PNG para perfil
                    DataSize.ofMegabytes(6),            // 2MB máximo
                    DataSize.ofKilobytes(10),           // 10KB mínimo
                    true
            ),
            ImageType.COVER, new ValidationRule(
                    List.of("image/jpeg", "image/png", "image/webp"), // + WEBP para capa
                    DataSize.ofMegabytes(6),            // 5MB máximo
                    DataSize.ofKilobytes(50),           // 50KB mínimo
                    true
            ),
            ImageType.GALLERY, new ValidationRule(
                    List.of("image/jpeg", "image/png", "image/gif", "image/webp"), // + GIF para galeria
                    DataSize.ofMegabytes(10),           // 10MB máximo
                    DataSize.ofKilobytes(100),          // 100KB mínimo
                    true
            )
    );

    // ✅ Regras específicas por tipo de owner
    private static final Map<OwnerType, ValidationRule> OWNER_TYPE_RULES = Map.of(
            OwnerType.USER, new ValidationRule(
                    List.of("image/jpeg", "image/png"), // User: formatos mais restritos
                    DataSize.ofMegabytes(3),
                    DataSize.ofKilobytes(1),
                    true
            ),
            OwnerType.ROOM, new ValidationRule(
                    List.of("image/jpeg", "image/png", "image/gif", "image/webp"), // Room: mais flexível
                    DataSize.ofMegabytes(15),
                    DataSize.ofKilobytes(1),
                    true
            ),
            OwnerType.PRODUCT, new ValidationRule(
                    List.of("image/jpeg", "image/png", "image/webp"), // Product: sem GIF
                    DataSize.ofMegabytes(8),
                    DataSize.ofKilobytes(5),
                    true
            )
    );

    // ✅ Método para obter regra combinada
    public static ValidationRule getRule(ImageType imageType, OwnerType ownerType) {
        ValidationRule imageRule = IMAGE_TYPE_RULES.get(imageType);
        ValidationRule ownerRule = OWNER_TYPE_RULES.get(ownerType);

        // Combina as regras (intersecção mais restritiva)
        return new ValidationRule(
                intersectContentTypes(imageRule.allowedContentTypes(), ownerRule.allowedContentTypes()),
                minSize(imageRule.maxSize(), ownerRule.maxSize()), // Pega o menor máximo
                maxSize(imageRule.minSize(), ownerRule.minSize()), // Pega o maior mínimo
                imageRule.required() && ownerRule.required()
        );
    }

    private static List<String> intersectContentTypes(List<String> list1, List<String> list2) {
        Set<String> set2 = new HashSet<>(list2);
        return list1.stream().filter(set2::contains).toList();
    }

    private static DataSize minSize(DataSize size1, DataSize size2) {
        return size1.toBytes() < size2.toBytes() ? size1 : size2;
    }

    private static DataSize maxSize(DataSize size1, DataSize size2) {
        return size1.toBytes() > size2.toBytes() ? size1 : size2;
    }
}
