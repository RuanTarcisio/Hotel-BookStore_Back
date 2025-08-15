package com.rtarcisio.hotelbookstore.room.mappers;

import com.rtarcisio.hotelbookstore.room.domains.Room;
import com.rtarcisio.hotelbookstore.room.dtos.inputs.InputRoom;
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
        room.setImageRoom(ImageMapper.mapToImage(input.getRoomImage()));

        return room;
    }



}
