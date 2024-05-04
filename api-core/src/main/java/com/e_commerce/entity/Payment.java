package com.e_commerce.entity;

import com.e_commerce.constant.PaymentType;
import com.e_commerce.dto.PaymentInfo;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class Payment extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cardInformation_id")
	private CardInformation cardInformation;

	@Enumerated(EnumType.STRING)
	private PaymentType paymentType;

	public static Payment toEntity(PaymentInfo paymentInfo) {
		return Payment.builder()
			.cardInformation(null)
			.paymentType(paymentInfo.paymentType())
			.build();
	}

	public void addCardInformation(CardInformation cardInformation) {
		this.cardInformation = cardInformation;
	}
}
