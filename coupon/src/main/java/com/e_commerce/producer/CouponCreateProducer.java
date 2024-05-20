package com.e_commerce.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.e_commerce.dto.CouponRequest;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class CouponCreateProducer {

	private final KafkaTemplate<String, CouponRequest> kafkaTemplate;

	public void create(Long userId, double discountRate) {
		CouponRequest couponRequest = new CouponRequest(userId, discountRate);
		kafkaTemplate.send("coupon_create", couponRequest);
	}
}
