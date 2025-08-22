package com.rtarcisio.hotelbookstore.room_boundary.repositories;

import com.rtarcisio.hotelbookstore.room_boundary.domains.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, String> {
}
