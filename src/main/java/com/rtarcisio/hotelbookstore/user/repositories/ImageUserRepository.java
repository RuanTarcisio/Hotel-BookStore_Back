package com.rtarcisio.hotelbookstore.user.repositories;

import com.rtarcisio.hotelbookstore.room.domains.ImageRoom;
import com.rtarcisio.hotelbookstore.user.domains.ImageUser;
import com.rtarcisio.hotelbookstore.user.domains.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;


public interface ImageUserRepository extends JpaRepository<ImageUser, String>{

    Optional<ImageUser> findByUser(User user);
}
