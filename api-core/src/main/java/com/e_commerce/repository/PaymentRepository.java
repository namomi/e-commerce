package com.e_commerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.e_commerce.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
	@Modifying
	@Query("DELETE FROM Payment p WHERE p.user.id = :userId")
	void deleteByUserId(Long userId);
}
