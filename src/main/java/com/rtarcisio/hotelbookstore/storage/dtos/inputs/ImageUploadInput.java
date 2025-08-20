package com.rtarcisio.hotelbookstore.storage.dtos.inputs;

import com.rtarcisio.hotelbookstore.storage.enums.ImageType;
import com.rtarcisio.hotelbookstore.storage.enums.OwnerType;
import com.rtarcisio.hotelbookstore.storage.validations.FileInput;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FileInput(required = true)
public class ImageUploadInput {
    @NotNull(message = "Arquivo é obrigatório")

    private MultipartFile file;

    @NotNull(message = "Tipo do owner é obrigatório")
    private OwnerType ownerType;

    @NotBlank(message = "ID do owner é obrigatório")
    private String ownerId;

    @NotNull(message = "Tipo da imagem é obrigatório")
    private ImageType imageType;

}
