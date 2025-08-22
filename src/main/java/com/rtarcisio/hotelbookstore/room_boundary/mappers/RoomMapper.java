package com.rtarcisio.hotelbookstore.room_boundary.mappers;

import com.rtarcisio.hotelbookstore.room_boundary.domains.Room;
import com.rtarcisio.hotelbookstore.room_boundary.dtos.inputs.InputRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoomMapper {

    public static Room inputToRoom(InputRoom input){

        Room room = new Room();
        room.setCapacity(input.getCapacity());
        room.setDescription(input.getDescription());
        room.setPrice(input.getPrice());
        room.setTitle(input.getTitle());
        room.setType(input.getType());
        room.setSize(input.getSize());
//        room.setImageRoom(ImageRoomMapper.mapToImage(input.getRoomImage()));

        return room;
    }



}
