package com.e_commerce.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.e_commerce.dto.CouponRequest;
import com.e_commerce.entity.Coupon;
import com.e_commerce.entity.FailedEvent;
import com.e_commerce.repository.CouponRepository;
import com.e_commerce.repository.FailedEventRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class CouponCreatedConsumer {

	private final CouponRepository couponRepository;

	private final FailedEventRepository failedEventRepository;

	@KafkaListener(topics = "coupon_create", groupId = "coupon-group")
	public void listener(CouponRequest request) {
		try {
			Coupon coupon = Coupon.createCoupon(request.userId(), request.discountRate());
			couponRepository.save(coupon);
			log.info("쿠폰이 생성 및 발급되었습니다. {}", coupon.getCode());
		} catch (Exception e) {
			log.error("쿠폰 생성 처리 중 오류 발생 {}", e.getMessage());
			failedEventRepository.save(new FailedEvent(request.userId()));
		}
	}
}
