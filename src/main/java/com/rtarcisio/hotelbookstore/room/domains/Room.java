package com.rtarcisio.hotelbookstore.room.domains;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;
    private String title;
    private String description;
    private String type;
    private int capacity;
    private Double price;
    private String size;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private Image imageRoom;

    private String urlImage;

    @ElementCollection
    @CollectionTable(name = "room_reservations", joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "reservation_id")
    private Set<UUID> reservations = new HashSet<>();
}
