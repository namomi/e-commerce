package com.e_commerce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.e_commerce.entity.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
	Optional<Coupon> findByCode(String code);

	List<Coupon> findByIssuedFalse();

	boolean existsByIssuedTo(String string);
}
