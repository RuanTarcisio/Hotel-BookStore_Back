package com.rtarcisio.hotelbookstore.reservation.domains;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String reservationId;
    private String firstName;
    private String lastName;
    private String email;
    private Date checkIn;
    private Date checkOut;
    private Long roomId;
}
