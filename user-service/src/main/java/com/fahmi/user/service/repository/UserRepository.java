package com.fahmi.user.service.repository;

import com.fahmi.user.service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    <T> Optional<T> findByEmailIgnoreCase(String trim);
}