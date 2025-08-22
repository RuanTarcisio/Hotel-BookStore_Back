package com.rtarcisio.hotelbookstore.storage_boundary.validations.images;

import com.rtarcisio.hotelbookstore.storage_boundary.enums.ImageType;
import com.rtarcisio.hotelbookstore.storage_boundary.enums.MediaTypeExtension;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class MediaTypeValidator {

    public void validateForImageType(MediaType mediaType, ImageType imageType) {
        MediaTypeExtension mediaTypeExt = MediaTypeExtension.fromMediaType(mediaType)
                .orElseThrow(() -> new RuntimeException(
                        "Tipo de mídia não suportado: " + mediaType
                ));

        switch (imageType) {
            case PROFILE:
                if (!mediaTypeExt.getMediaType().getType().equals("image")) {
                    throw new RuntimeException(
                            "Imagem de perfil deve ser do tipo image, não " + mediaTypeExt.getMediaType().getType()
                    );
                }
                break;

            case COVER:
                // Validações específicas para capa
                if (mediaTypeExt == MediaTypeExtension.GIF) {
                    throw new RuntimeException("GIF não é permitido para capas");
                }
                break;

            case GALLERY:
                // Validações específicas para galeria
                break;

            default:
                throw new IllegalArgumentException("Tipo de imagem desconhecido: " + imageType);
        }
    }
}