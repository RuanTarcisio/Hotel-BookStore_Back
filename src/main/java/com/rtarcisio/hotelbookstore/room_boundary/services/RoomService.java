package com.rtarcisio.hotelbookstore.room_boundary.services;


import com.rtarcisio.hotelbookstore.reservation_boundary.services.ReservationService;
import com.rtarcisio.hotelbookstore.room_boundary.domains.Room;
import com.rtarcisio.hotelbookstore.room_boundary.dtos.RoomDTO;
import com.rtarcisio.hotelbookstore.room_boundary.dtos.inputs.InputRoom;
import com.rtarcisio.hotelbookstore.room_boundary.mappers.RoomMapper;
import com.rtarcisio.hotelbookstore.room_boundary.repositories.RoomRepository;
import com.rtarcisio.hotelbookstore.shared_boundary.clients.ImageClientService;
import com.rtarcisio.hotelbookstore.shared_boundary.exceptions.ObjetoNaoEncontradoException;
import com.rtarcisio.hotelbookstore.storage_boundary.dtos.inputs.ImageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomService {

    private final RoomRepository roomRepository;
    private final ImageClientService imageClientService;
    private final ReservationService reservationService;

    @Value("${app.base-url}")
    private String apiUrl;

    private Optional<Room> getRoom(String id) {
        return roomRepository.findById(id);
    }

    public RoomDTO getRoomDTO(String id) {
        Room room = roomRepository.findById(id).orElseThrow(NoSuchElementException::new);
        return RoomDTO.fromRoom(room);
    }

    public List<RoomDTO> getRoomsDTO() {
        List<Room> rooms = roomRepository.findAll();
        return rooms.stream().map(RoomDTO::fromRoom).toList();
    }

    public RoomDTO save(InputRoom inputRoom, String token) {
        Room room = RoomMapper.inputToRoom(inputRoom);
        room = roomRepository.save(room);

        if (inputRoom.getRoomImage() != null) {
            try {
                ImageResponse response = imageClientService.uploadImage(
                        inputRoom.getRoomImage(),
                        "ROOM",
                        room.getRoomId(),
                        "GALLERY",
                        token);

                room.setUrlImageRoom(response.getUrlImage());
                roomRepository.save(room);

                log.info("Imagem de perfil atualizada para o quarto: {}", room.getRoomId());

            } catch (Exception e) {
                log.error("Falha ao fazer upload da imagem de perfil", e);
                throw new RuntimeException("Falha no upload da imagem: " + e.getMessage(), e);
            }
        }
        return RoomDTO.fromRoom(room);
    }


    public Room getRoomReservation(Long id, Long idReservation) {
        return null;
        //verificar se o id da reserva existe no quarto, se não existir ja retorna exception
        //se existir chamar reservationService
    }

    private String buildImageRoomUrl(Room room) {
//        if (room.getRoomId() == null || room.getImageRoom() == null || room.getImageRoom().getId() == null) {
//            throw new IllegalStateException("Room or Image not properly initialized");
//        }
        return String.format("%s/v1/rooms/%d/photo/%s", apiUrl, room.getRoomId());
//                room.getImageRoom().getId());
    }

    public void uploadRoomImage(MultipartFile file, String token, String roomId) {
        Room room = getRoom(roomId).orElseThrow(() -> new ObjetoNaoEncontradoException("Nenhum quarto foi encontrado"));

        try {
            ImageResponse response = imageClientService.uploadImage(
                    file,
                    "ROOM",
                    room.getRoomId(),
                    "GALLERY",
                    token);

            room.setUrlImageRoom(response.getUrlImage());
            roomRepository.save(room);

            log.info("Imagem de perfil atualizada para o quarto: {}", room.getRoomId());

        } catch (Exception e) {
            log.error("Falha ao fazer upload da imagem de perfil", e);
            throw new RuntimeException("Falha no upload da imagem: " + e.getMessage(), e);
        }
    }
}
