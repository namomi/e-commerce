package com.e_commerce.repository;

import com.e_commerce.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {

	boolean existsByUsernameOrEmail(String username, String email);

    Optional<Users> findByUsername(String username);
}
