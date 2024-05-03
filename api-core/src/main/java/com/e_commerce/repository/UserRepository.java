package com.e_commerce.repository;

import com.e_commerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

	boolean existsByUsernameOrEmail(String username, String email);

    Optional<User> findByUsername(String username);
}
