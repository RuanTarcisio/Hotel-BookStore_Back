package com.rtarcisio.hotelbookstore.room.services;


import com.rtarcisio.hotelbookstore.room.domains.Image;
import com.rtarcisio.hotelbookstore.room.domains.Room;
import com.rtarcisio.hotelbookstore.room.dtos.RoomDTO;
import com.rtarcisio.hotelbookstore.room.dtos.inputs.InputRoom;
import com.rtarcisio.hotelbookstore.room.mappers.RoomMapper;
import com.rtarcisio.hotelbookstore.room.repositories.ImageRepository;
import com.rtarcisio.hotelbookstore.room.repositories.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final ImageRepository imageRepository;

    @Value("${api.url}")
    private String apiUrl;

    public Room getRoom(Long id) {
        return roomRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public List<Room> getRooms() {
        return roomRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Image> getImageById(String id) {
        return imageRepository.findById(id);
    }

    public RoomDTO save(InputRoom dto) {
        Room room = RoomMapper.inputToRoom(dto);

        if (room.getImageRoom() != null) {
            room.getImageRoom().setRoom(room); // garante bidirecional
        }

        room = roomRepository.save(room);

        if (room.getImageRoom() != null) {
            room.setUrlImage(buildImageRoomUrl(room)); // agora ID existe
            room = roomRepository.save(room); // update com a URL
        }

        return RoomDTO.fromRoom(room);
    }


    public Room getRoomReservation(Long id, Long idReservation) {
        return null;
        //verificar se o id da reserva existe no quarto, se não existir ja retorna exception
        //se existir chamar reservationService
    }

    private String buildImageRoomUrl(Room room) {
        if (room.getRoomId() == null || room.getImageRoom() == null || room.getImageRoom().getId() == null) {
            throw new IllegalStateException("Room or Image not properly initialized");
        }
        return String.format("%s/v1/rooms/%d/photo/%s",
                apiUrl,
                room.getRoomId(),
                room.getImageRoom().getId());
    }

}
