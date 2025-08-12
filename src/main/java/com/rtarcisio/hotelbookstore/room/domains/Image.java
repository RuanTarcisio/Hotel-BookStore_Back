package com.rtarcisio.hotelbookstore.room.domains;

import com.rtarcisio.hotelbookstore.room.domains.enums.ImageExtension;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Blob;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private Long size;

    @Enumerated(EnumType.STRING)
    private ImageExtension extension;

    @Column
    @CreatedDate
    private LocalDateTime uploadDate;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] file;

    @OneToOne(mappedBy = "imageRoom")
    private Room room;

}