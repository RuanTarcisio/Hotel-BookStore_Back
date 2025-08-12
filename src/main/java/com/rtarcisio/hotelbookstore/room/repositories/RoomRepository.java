package com.rtarcisio.hotelbookstore.room.repositories;

import com.rtarcisio.hotelbookstore.room.domains.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
