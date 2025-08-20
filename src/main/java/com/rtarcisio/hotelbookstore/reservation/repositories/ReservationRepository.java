package com.rtarcisio.hotelbookstore.reservation.repositories;

import com.rtarcisio.hotelbookstore.reservation.domains.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, String> {
}
