package com.e_commerce.dto;

import com.e_commerce.entity.CardInformation;
import com.e_commerce.entity.Payment;

public record CardInfo(String cardHolderName, String cardNumber, String expiryDate, String cvv) {
	public static CardInfo fromPayment(Payment payment) {
		CardInformation cardInformation = payment.getCardInformation();
		return new CardInfo(
			cardInformation.getCardHolderName(),
			cardInformation.getCardNumber(),
			cardInformation.getCardExpiry(),
			cardInformation.getCardCvv()
		);
	}
}
