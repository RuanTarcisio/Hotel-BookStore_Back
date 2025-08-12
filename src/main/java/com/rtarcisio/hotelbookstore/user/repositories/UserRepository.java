package com.rtarcisio.hotelbookstore.user.repositories;

import com.rtarcisio.hotelbookstore.user.domains.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
