package com.rtarcisio.hotelbookstore.user.mappers;

import com.rtarcisio.hotelbookstore.room.domains.ImageRoom;
import com.rtarcisio.hotelbookstore.room.dtos.ImageDTO;
import com.rtarcisio.hotelbookstore.shared.enums.ImageExtension;
import com.rtarcisio.hotelbookstore.shared.models.Image;
import com.rtarcisio.hotelbookstore.user.domains.ImageUser;
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
public class ImageUserMapper {

//    public static ImageRoom mapToImage(MultipartFile file)   {
//        ImageRoom image = new ImageRoom();
//        image.setSize(file.getSize());
//
//        try {
//            // Converte o MultipartFile para Blob
//            Blob blob = new SerialBlob(file.getBytes());
//            image.setFile(file.getBytes());
//        } catch (SQLException e) {
//            throw new RuntimeException("Erro ao converter arquivo para Blob", e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        image.setUploadDate(LocalDateTime.now());
//        image.setExtension(ImageExtension.valueOf(MediaType.valueOf(file.getContentType())));
//
//        return image;
//    }

    public static ImageUser mapImageToImageUser(Image image){
        if(image == null){
            return null;
        }

        ImageUser imageUser = new ImageUser();
        imageUser.setFile(image.getFile());
        imageUser.setExtension(image.getExtension());
        imageUser.setUploadDate(image.getUploadDate());
        imageUser.setOriginalFilename(image.getOriginalFilename());
        imageUser.setSize(image.getSize());
        return imageUser;
    }

    public static ImageUser mapToImage(MultipartFile file) {

        if(file == null) {return null;}

        ImageUser image = new ImageUser();

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

    public static ImageDTO imageToDTO(ImageRoom image, String url) {
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