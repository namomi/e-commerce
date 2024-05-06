package com.e_commerce.entity;

import com.e_commerce.dto.CardInfo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class CardInformation extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String cardHolderName;
	@Column(nullable = false)
	private String cardNumber;

	@Column(nullable = false)
	private String cardExpiry;

	@Column(nullable = false)
	private String cardCvv;

	public static CardInformation toEntity(CardInfo cardInfo) {
		return CardInformation.builder()
			.cardHolderName(cardInfo.cardHolderName())
			.cardNumber(cardInfo.cardNumber())
			.cardExpiry(cardInfo.expiryDate())
			.cardCvv(cardInfo.cvv())
			.build();
	}
}
