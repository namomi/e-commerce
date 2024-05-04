package com.e_commerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.e_commerce.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
