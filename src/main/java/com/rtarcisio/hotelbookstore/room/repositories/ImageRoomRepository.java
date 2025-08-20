package com.rtarcisio.hotelbookstore.room.repositories;

import com.rtarcisio.hotelbookstore.room.domains.ImageRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;



public interface ImageRoomRepository extends JpaRepository<ImageRoom, String>, JpaSpecificationExecutor<ImageRoom> {

}
