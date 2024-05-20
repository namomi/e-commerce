package com.e_commerce.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class Coupon extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	private String code;

	@NotNull
	private boolean issued;

	private String issuedTo;

	private LocalDateTime issuedAt;

	@NotNull
	private double discountRate;

	public static Coupon createCoupon(Long userId, double discountRate) {
		return Coupon.builder()
			.code(generateCouponCode())
			.issued(true)
			.issuedTo(userId.toString())
			.issuedAt(LocalDateTime.now())
			.discountRate(discountRate)
			.build();
	}

	private static String generateCouponCode() {
		// 쿠폰 코드 생성 로직 (예: UUID)
		return "COUPON-" + UUID.randomUUID().toString();
	}
}
