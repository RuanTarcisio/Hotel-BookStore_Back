package com.rtarcisio.hotelbookstore.reservation.services;


import com.rtarcisio.hotelbookstore.room.domains.ImageRoom;
import com.rtarcisio.hotelbookstore.room.domains.Room;
import com.rtarcisio.hotelbookstore.room.dtos.RoomDTO;
import com.rtarcisio.hotelbookstore.room.dtos.inputs.InputRoom;
import com.rtarcisio.hotelbookstore.room.mappers.RoomMapper;
import com.rtarcisio.hotelbookstore.room.repositories.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ReservationService {

    private final RoomRepository roomRepository;

    public Room getRoom(Long id) {
        return roomRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public List <Room> getRooms() {
        return roomRepository.findAll();
    }

    public Optional<ImageRoom> getImageById(String id) {
        return null;  //imageRoomRepository.findById(id);

    }

    public RoomDTO save(InputRoom dto){
        Room room = RoomMapper.inputToRoom(dto);
        room = roomRepository.save(room);
        return RoomDTO.fromRoom(room);
    }
}
