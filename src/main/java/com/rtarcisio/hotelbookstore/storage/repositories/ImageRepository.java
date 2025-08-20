package com.rtarcisio.hotelbookstore.storage.repositories;

import com.rtarcisio.hotelbookstore.storage.domains.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, String> {
    List<Image> findByOwnerTypeAndOwnerId(String ownerType, String ownerId);

    Optional<Image> findByOwnerTypeAndOwnerIdAndImageType(
            String ownerType, String ownerId, String imageType);
}