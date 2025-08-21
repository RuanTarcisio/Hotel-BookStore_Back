package com.rtarcisio.hotelbookstore.shared.dtos;

import com.rtarcisio.hotelbookstore.storage.validations.FileInput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FileInput(required = true)
public class ImageUploadInput {
//    @NotNull(message = "Arquivo é obrigatório")
    private MultipartFile file;

//    @NotNull(message = "Tipo do owner é obrigatório")
    private String ownerType;

//    @NotBlank(message = "ID do owner é obrigatório")
    private String ownerId;

//    @NotNull(message = "Tipo da imagem é obrigatório")
    private String imageType;

}
