package com.rtarcisio.hotelbookstore.reservation_boundary.repositories;

import com.rtarcisio.hotelbookstore.reservation_boundary.domains.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, String> {
}
