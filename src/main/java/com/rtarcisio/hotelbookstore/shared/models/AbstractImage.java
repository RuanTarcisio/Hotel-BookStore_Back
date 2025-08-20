package com.rtarcisio.hotelbookstore.shared.models;

import com.rtarcisio.hotelbookstore.shared.enums.ImageExtension;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@MappedSuperclass
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractImage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private Long size;

    @Enumerated(EnumType.STRING)
    private ImageExtension extension;

    @Column
    @CreatedDate
    private LocalDateTime uploadDate;

    private String originalFilename;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] file;

}