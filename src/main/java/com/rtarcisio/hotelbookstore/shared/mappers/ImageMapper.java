package com.rtarcisio.hotelbookstore.shared.mappers;

import com.rtarcisio.hotelbookstore.room.domains.ImageRoom;
import com.rtarcisio.hotelbookstore.shared.enums.ImageExtension;
import com.rtarcisio.hotelbookstore.shared.models.Image;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Component
public class ImageMapper {

    public static Image mapToImage(MultipartFile file) {

        if(file == null) {return null;}

        Image image = new Image();

        try {
            image.setFile(file.getBytes());
            image.setSize(file.getSize());
            image.setOriginalFilename(file.getOriginalFilename());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        image.setUploadDate(LocalDateTime.now());

        MediaType contentType = MediaType.valueOf(file.getContentType());
        ImageExtension extension = ImageExtension.valueOf(contentType);

        if (extension == null) {
            throw new IllegalArgumentException("Tipo de arquivo não suportado: " + contentType);
        }

        image.setExtension(extension);

        return image;
    }
}
