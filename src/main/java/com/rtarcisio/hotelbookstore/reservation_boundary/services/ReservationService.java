package com.rtarcisio.hotelbookstore.reservation_boundary.services;


import com.rtarcisio.hotelbookstore.room_boundary.domains.Room;
import com.rtarcisio.hotelbookstore.room_boundary.dtos.RoomDTO;
import com.rtarcisio.hotelbookstore.room_boundary.dtos.inputs.InputRoom;
import com.rtarcisio.hotelbookstore.room_boundary.mappers.RoomMapper;
import com.rtarcisio.hotelbookstore.room_boundary.repositories.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;


@Service
@RequiredArgsConstructor
public class ReservationService {

    private final RoomRepository roomRepository;

    public Room getRoom(String id) {
        return roomRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public List <Room> getRooms() {
        return roomRepository.findAll();
    }


    public RoomDTO save(InputRoom dto){
        Room room = RoomMapper.inputToRoom(dto);
        room = roomRepository.save(room);
        return RoomDTO.fromRoom(room);
    }
}
