package com.rtarcisio.hotelbookstore.room.dtos;

import com.rtarcisio.hotelbookstore.room.domains.Room;

public record RoomDTO (Long roomId, String title, String description, String type){

    public static RoomDTO fromRoom(Room room) {
        return new RoomDTO(room.getRoomId(), room.getTitle(), room.getDescription(), room.getType());
    }
}
