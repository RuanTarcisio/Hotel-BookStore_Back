package com.rtarcisio.hotelbookstore.room.repositories;

import com.rtarcisio.hotelbookstore.room.domains.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;



public interface ImageRepository extends JpaRepository<Image, String>, JpaSpecificationExecutor<Image> {

}
