package com.rtarcisio.hotelbookstore.room.mappers;

import com.rtarcisio.hotelbookstore.room.domains.Image;
import com.rtarcisio.hotelbookstore.room.domains.enums.ImageExtension;
import com.rtarcisio.hotelbookstore.room.dtos.ImageDTO;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Component
public class ImageMapper {

    public static Image mapToImage(MultipartFile file)   {
        Image image = new Image();
        image.setSize(file.getSize());

        try {
            // Converte o MultipartFile para Blob
            Blob blob = new SerialBlob(file.getBytes());
            image.setFile(file.getBytes());
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao converter arquivo para Blob", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        image.setUploadDate(LocalDateTime.now());
        image.setExtension(ImageExtension.valueOf(MediaType.valueOf(file.getContentType())));

        return image;
    }

//    public static ImageUser mapToImage(MultipartFile file) {
//        ImageUser image = new ImageUser();
//
//        try {
//            image.setName(file.getOriginalFilename()); // Use getOriginalFilename() para o nome do arquivo
//            image.setSize(file.getSize());
//            image.setFile(new SerialBlob(file.getBytes()));
//            image.setTags("image_User");
//            image.setUploadDate(LocalDateTime.now());
//            image.setExtension(ImageExtension.valueOf(MediaType.valueOf(file.getContentType())));
//        } catch (IOException | SQLException e) {
//            throw new RuntimeException("Erro ao converter arquivo para Blob", e);
//        }
//
//        return image;
//    }

    public static ImageDTO imageToDTO(Image image, String url) {
        return ImageDTO.builder()
                .url(url)
                .extension(image.getExtension().name())
                .size(image.getSize())
                .uploadDate(image.getUploadDate().toLocalDate())
                .build();
    }

    public static byte[] readBlobToBytes(Blob blob) throws SQLException, IOException {
        if (blob == null) {
            return null; // ou throw new IllegalArgumentException("Blob não pode ser nulo");
        }

        try (InputStream inputStream = blob.getBinaryStream()) {
            long blobLength = blob.length();

            if (blobLength > Integer.MAX_VALUE) {
                throw new IOException("Tamanho do BLOB excede o limite máximo de 2GB");
            }

            byte[] bytes = new byte[(int) blobLength];
            int bytesRead = inputStream.read(bytes);

            if (bytesRead != blobLength) {
                throw new IOException("Falha ao ler todos os bytes do BLOB (lidos " + bytesRead + " de " + blobLength + ")");
            }

            return bytes;
        }
    }

}