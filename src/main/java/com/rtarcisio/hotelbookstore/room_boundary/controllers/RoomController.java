package com.rtarcisio.hotelbookstore.room_boundary.controllers;


import com.rtarcisio.hotelbookstore.room_boundary.domains.Room;
import com.rtarcisio.hotelbookstore.room_boundary.dtos.RoomDTO;
import com.rtarcisio.hotelbookstore.room_boundary.dtos.inputs.InputRoom;
import com.rtarcisio.hotelbookstore.room_boundary.services.RoomService;
import com.rtarcisio.hotelbookstore.shared_boundary.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Tag(name = "Rooms", description = "Endpoints para gerenciamento de quartos")
@RestController
@RequestMapping(value = {"/v1/rooms", "/v1/rooms/"})
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @Operation(summary = "Cadastrar novo quarto", description = "Realiza o cadastro de um novo quarto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Quarto criado com sucesso"),
            @ApiResponse(responseCode = "409", description = "Quarto já existe"),
    })
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> save(
            @Parameter(description = "Dados do quarto para cadastro", required = true)
            @ModelAttribute @Valid InputRoom inputRoom, HttpServletRequest request) {

        String token = SecurityUtils.extractTokenFromCookie(request);
        if (token == null) {
            throw new AccessDeniedException("Usuario não autenticado.");
        }

        RoomDTO roomDTO = roomService.save(inputRoom, token);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(roomDTO.roomId())
                .toUri();
        return ResponseEntity.created(uri).build();

    }

    @GetMapping("/{id}")
//    @CacheControl(maxAge = 30, maxAgeUnit = TimeUnit.SECONDS)
    public ResponseEntity<RoomDTO> getRoom(
            @Parameter(description = "ID do quarto", required = true, example = "1")
            @PathVariable String id) {
        return ResponseEntity.ok().body(roomService.getRoomDTO(id));
    }

    @GetMapping("")
//    @CacheControl(maxAge = 30, maxAgeUnit = TimeUnit.SECONDS)
    public ResponseEntity<List<RoomDTO>> getAllRoom() {
        return ResponseEntity.ok().body(roomService.getRoomsDTO());
    }

    @GetMapping("/{id}/reservation/{id_reservation}")
//    @CacheControl(maxAge = 30, maxAgeUnit = TimeUnit.SECONDS)
    public ResponseEntity<Room> getRoomReservation(
            @Parameter(description = "ID do quarto", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "ID da reserva", required = true, example = "1")
            @PathVariable Long id_reservation) {
        return ResponseEntity.ok().body(roomService.getRoomReservation(id, id_reservation));
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()") // ✅ Autenticação básica
    public ResponseEntity<?/*ImageResponse*/> uploadImage(@RequestPart MultipartFile file, @RequestParam String roomId, HttpServletRequest request) {

        String token = SecurityUtils.extractTokenFromCookie(request);
        if (token == null) {
            throw new AccessDeniedException("Usuario não autenticado.");
        }

        roomService.uploadRoomImage(file, token, roomId);
        return ResponseEntity.ok().build();
    }
//    @Operation(summary = "Recuperar imagem por ID", description = "Retorna o conteúdo binário da imagem")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Imagem encontrada"),
//            @ApiResponse(responseCode = "404", description = "Imagem não encontrada"),
//            @ApiResponse(responseCode = "400", description = "Erro ao processar o arquivo")
//    })
//    @GetMapping(value = "/{id}/photo/{id_photo}")
//    public ResponseEntity<byte[]> find(
//            @Parameter(description = "ID do room") @PathVariable String id, @Parameter(description = "ID do room") @PathVariable String id_photo) {
//
//        Optional<ImageRoom> possibleImage = roomService.getImageById(id_photo);
//        if (possibleImage.isEmpty() || possibleImage.get().getFile() == null) {
//            return ResponseEntity.notFound().build();
//        }
//
//        ImageRoom image = possibleImage.get();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(image.getExtension().getMediaType());
//        headers.setContentLength(image.getSize());
//        headers.setContentDisposition(ContentDisposition.inline().build());
//        headers.setCacheControl(CacheControl.maxAge(30, TimeUnit.DAYS));
//
//        return new ResponseEntity<>(image.getFile(), headers, HttpStatus.OK);
//    }

    @GetMapping("/teste")
    public ResponseEntity<String> teste() {
        System.out.println("Entrou no método teste!");

        return ResponseEntity.ok().body("Bateu aqui");
    }
}
