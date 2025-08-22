package com.rtarcisio.hotelbookstore.user_boundary.repositories;

import com.rtarcisio.hotelbookstore.user_boundary.domains.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);

    Optional<User> findByCpf(String cpf);

    Optional<User> findByAuthUserId(String id);

    @Query("SELECT u.userId FROM User u WHERE u.userId = :id")
    String findJustUserIdById(@Param("id") String id);

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);

    boolean existsByEmailOrCpf(String email, String cpf);


}
