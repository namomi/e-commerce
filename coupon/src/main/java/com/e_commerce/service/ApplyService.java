package com.e_commerce.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.e_commerce.producer.CouponCreateProducer;
import com.e_commerce.repository.AppliedUserRepository;
import com.e_commerce.repository.CouponCountRepository;
import com.e_commerce.repository.CouponRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ApplyService {

	private final CouponRepository couponRepository;
	private final CouponCountRepository couponCountRepository;
	private final CouponCreateProducer couponCreateProducer;
	private final AppliedUserRepository appliedUserRepository;

	@Transactional
	public void apply(Long userId, double discountRate) {

		boolean hasCoupon = couponRepository.existsByIssuedTo(userId.toString());

		if (hasCoupon)
			return;

		Boolean apply = appliedUserRepository.add(userId);

		if (!apply)
			return;

		Long count = couponCountRepository.increment();

		if (count > 100)
			return;

		couponCreateProducer.create(userId, discountRate);
	}
}
