package com.rtarcisio.hotelbookstore.storage_boundary.services;

import com.rtarcisio.hotelbookstore.auth_boundary.domains.AuthUser;
import com.rtarcisio.hotelbookstore.shared_boundary.dtos.ImageUploadInput;
import com.rtarcisio.hotelbookstore.shared_boundary.exceptions.ImageNotFoundException;
import com.rtarcisio.hotelbookstore.storage_boundary.domains.Image;
import com.rtarcisio.hotelbookstore.storage_boundary.dtos.inputs.ImageResponse;
import com.rtarcisio.hotelbookstore.storage_boundary.dtos.inputs.UploadContext;
import com.rtarcisio.hotelbookstore.storage_boundary.enums.ImageType;
import com.rtarcisio.hotelbookstore.storage_boundary.enums.MediaTypeExtension;
import com.rtarcisio.hotelbookstore.storage_boundary.enums.OwnerType;
import com.rtarcisio.hotelbookstore.storage_boundary.repositories.ImageRepository;
import com.rtarcisio.hotelbookstore.storage_boundary.validations.images.ImageValidator;
import com.rtarcisio.hotelbookstore.storage_boundary.validations.images.MediaTypeValidator;
import com.rtarcisio.hotelbookstore.user_boundary.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
@Transactional
public class ImageService {
    private final ImageRepository imageRepository;
    private final FileStorageService storageService;
    private final MediaTypeValidator mediaTypeValidator;
    private final ImageValidator imageValidator;
    private final UserService userService;

    @Value("${app.base-url}")
    private String apiUrl;

    public Image uploadImage(ImageUploadInput input, AuthUser authUser) {
        // 1. VALIDAÇÃO (separado)
        imageValidator.validateUpload(input, authUser);

        // 2. CONVERSÃO (separado)
        UploadContext context = convertToContext(input, authUser.getAuthUserId());

        // 3. STORAGE (separado)
        String storagePath = storageService.storeFile(context);

        // 4. CRIAÇÃO DA ENTIDADE (separado)
        Image image = createImageEntity(context, storagePath);

        // 5. PERSISTÊNCIA
        image = imageRepository.save(image);

        // 6. URL (separado)
        image.setImgUrl(generateImageUrl(image.getId()));

        return imageRepository.save(image);
    }

    private UploadContext convertToContext(ImageUploadInput input, String authUserId) {
        return new UploadContext(
                MediaType.valueOf(input.getFile().getContentType()),
                ImageType.valueOf(input.getImageType()),
                OwnerType.valueOf(input.getOwnerType()),
                input.getFile(),
                authUserId,
                input.getOwnerId()
        );
    }

    private Image createImageEntity(UploadContext context, String storagePath) {
        Image image = new Image();
        image.setOwnerType(context.ownerType());
        image.setOwnerId(context.ownerId());
        image.setUploaderId(context.uploaderId());
        image.setImageType(context.imageType());
        image.setOriginalFilename(context.file().getOriginalFilename());
        image.setSize(context.file().getSize());
        image.setStoragePath(storagePath);
        image.setCreatedAt(LocalDateTime.now());
        image.setExtension(MediaTypeExtension.fromMediaType(context.contentType())
                .orElseThrow());
        return image;
    }

    private String generateImageUrl(String imageId) {
        return apiUrl + "/v1/images/" + imageId;
    }
    public Image uploadImage(MultipartFile file, OwnerType ownerType,
                             String ownerId, ImageType imageType) {

        MediaType contentType = MediaType.valueOf(file.getContentType());

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

//    public Image uploadImage(@Valid ImageUploadInput input, AuthUser authentication) {
//
//        validateImageTypePermissions(ImageType.valueOf(input.getImageType()), authentication);
//
//        MediaType contentType = MediaType.valueOf(input.getFile().getContentType());
//        ImageType imageType = ImageType.valueOf(input.getImageType());
//        OwnerType ownerType = OwnerType.valueOf(input.getOwnerType());
//        MultipartFile file = input.getFile();
//        String ownerId = input.getOwnerId();
//
//        String storagePath = storageService.storeFile(file, ownerType, ownerId, imageType);
//
//        MediaTypeExtension mediaTypeExt = MediaTypeExtension.fromMediaType(contentType)
//                .orElseThrow(); // Já validado acima, não deveria falhar
//
//        Image image = new Image();
//        image.setOwnerType(ownerType);
//        image.setOwnerId(ownerId);
//        image.setImageType(imageType);
//        image.setOriginalFilename(file.getOriginalFilename());
//        image.setSize(file.getSize());
//        image.setStoragePath(storagePath);
//        image.setCreatedAt(LocalDateTime.now());
//        image.setExtension(mediaTypeExt); // ✅ Usando MediaTypeExtension diretamente
//
//        image = imageRepository.save(image);
//        image.setImgUrl(apiUrl + "/v1/images/" + image.getId());
//        return imageRepository.save(image);
//    }


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

    public ImageResponse addUrlImage(Image image) {
        return null;
    }

    private void validateImageTypePermissions(ImageType imageType, AuthUser auth) {
        switch (imageType) {
            case PROFILE:
            case COVER:
                if (!auth.hasRole("USER") && !auth.hasRole("ADMIN")) {
                    throw new AccessDeniedException("Sem permissão para upload de imagem de perfil/capa");
                }
                break;

            case GALLERY:
                if (!auth.hasRole("ADMIN")) {
                    throw new AccessDeniedException("Apenas administradores podem uploadar imagens de galeria");
                }
                break;

            default:
                throw new IllegalArgumentException("Tipo de imagem não suportado: " + imageType);
        }
    }
}