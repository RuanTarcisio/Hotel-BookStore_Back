package com.rtarcisio.hotelbookstore.auth.repositories;

import com.rtarcisio.hotelbookstore.auth.domains.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {
    Optional<AuthUser> findByEmail(String email);
}
