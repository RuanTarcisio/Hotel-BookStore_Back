package com.rtarcisio.hotelbookstore.storage.services;

import com.rtarcisio.hotelbookstore.storage.domains.Image;
import com.rtarcisio.hotelbookstore.storage.enums.ImageType;
import com.rtarcisio.hotelbookstore.storage.enums.MediaTypeExtension;
import com.rtarcisio.hotelbookstore.storage.enums.OwnerType;
import com.rtarcisio.hotelbookstore.storage.repositories.ImageRepository;
import com.rtarcisio.hotelbookstore.storage.validations.MediaTypeValidator;
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

        // 1. Validações iniciais
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Arquivo vazio");
        }

        // 2. Validação de tipo de mídia
        MediaType contentType = MediaType.valueOf(file.getContentType());
        mediaTypeValidator.validateForImageType(contentType, imageType);

        // 3. Salvar arquivo
        String storagePath = storageService.storeFile(file, ownerType, ownerId, imageType);

        // 4. Criar metadados
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


//    public Image uploadImage(MultipartFile file, String ownerType,
//                             String ownerId, String imageType) {
//        if (file.isEmpty()) {
//            throw new RuntimeException("Arquivo vazio");
//        }
//
//        Image image = new Image();
//        try {
//            image.setFile(file.getBytes());
//            image.setSize(file.getSize());
//            image.setOriginalFilename(file.getOriginalFilename());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        image.setCreatedAt(LocalDateTime.now());
//        image.setOwnerType(ownerType);
//        image.setOwnerId(ownerId);
//        image.setImageType(imageType);
//
//        MediaType contentType = MediaType.valueOf(file.getContentType());
//        ImageExtension extension = ImageExtension.valueOf(contentType);
//
//        if (extension == null) {
//            throw new IllegalArgumentException("Tipo de arquivo não suportado: " + contentType);
//        }
//        image.setExtension(extension);
//        image.setStoragePath();
//        imageRepository.save(image);
//
//        return imageRepository.save(image);
//    }

}