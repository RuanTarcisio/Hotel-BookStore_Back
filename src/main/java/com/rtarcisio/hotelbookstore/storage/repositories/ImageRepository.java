package com.rtarcisio.hotelbookstore.storage.repositories;

import com.rtarcisio.hotelbookstore.storage.domains.Image;
import com.rtarcisio.hotelbookstore.storage.enums.ImageType;
import com.rtarcisio.hotelbookstore.storage.enums.OwnerType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, String> {
    List<Image> findByOwnerTypeAndOwnerId(String ownerType, String ownerId);

    Optional<Image> findByOwnerTypeAndOwnerIdAndImageType(
            String ownerType, String ownerId, String imageType);

    Optional<Image> findByOwnerTypeAndOwnerIdAndImageType(OwnerType ownerType, String ownerId, ImageType imageType);
}