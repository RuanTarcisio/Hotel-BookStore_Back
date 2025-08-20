package com.rtarcisio.hotelbookstore.user.repositories;

import com.rtarcisio.hotelbookstore.user.domains.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);

    Optional<User> findByCpf(String cpf);

    Optional<User> findByAuthUserId(String id);

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);

    boolean existsByEmailOrCpf(String email, String cpf);


}
