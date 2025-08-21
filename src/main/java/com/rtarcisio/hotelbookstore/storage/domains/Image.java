package com.rtarcisio.hotelbookstore.storage.domains;

import com.rtarcisio.hotelbookstore.storage.enums.ImageType;
import com.rtarcisio.hotelbookstore.storage.enums.MediaTypeExtension;
import com.rtarcisio.hotelbookstore.storage.enums.OwnerType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "images", indexes = {
        @Index(name = "idx_image_owner", columnList = "ownerType,ownerId"),
        @Index(name = "idx_image_type", columnList = "ownerType,ownerId,imageType")
})
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Enumerated(EnumType.STRING)
    private OwnerType ownerType; // ✅ Enum correto

    private String ownerId;

    @Enumerated(EnumType.STRING)
    private ImageType imageType; // ✅ Enum correto

    @Enumerated(EnumType.STRING)
    private MediaTypeExtension extension;
    private Long size;
    private String storagePath; // ✅ APENAS caminho/URL
    private LocalDateTime createdAt;
    private String originalFilename;

    private String imgUrl;


//    public void setStoragePath() {
//       switch(ownerType){
//           case ("PROFILE"): this.storagePath = "PROFILE"; break;
//           case ("COVER"): this.storagePath = "COVER"; break;
//           case ("GALLERY"): this.storagePath = "GALLERY"; break;
//       }
//    }
}
