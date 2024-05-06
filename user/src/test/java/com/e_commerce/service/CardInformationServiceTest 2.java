package com.e_commerce.service;

import static com.e_commerce.constant.PaymentType.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.e_commerce.dto.CardInfo;
import com.e_commerce.entity.CardInformation;
import com.e_commerce.entity.Payment;
import com.e_commerce.repository.CardInformationRepository;
import com.e_commerce.repository.PaymentRepository;

import e_commerce.service.CardInformationService;

class CardInformationServiceTest {

	@Mock
	private PaymentRepository paymentRepository;

	@Mock
	private CardInformationRepository cardInformationRepository;

	@InjectMocks
	private CardInformationService cardInformationService;

	private Payment payment;
	private CardInformation cardInformation;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		cardInformation = new CardInformation(1L, "hana", "1111-2222-3333-4444", "08/28", "018");
		payment = new Payment(1L, cardInformation, CARD);
	}

	@Test
	void addCardInfoSuccessTest() {
		// given
		Long paymentId = 1L;
		when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

		// when
		cardInformationService.addCardInfo(paymentId, cardInformation);

		// then
		verify(cardInformationRepository).save(cardInformation);
	}

	@Test
	void findByCardInfoSuccessTest() {
		// given
		Long paymentId = 1L;
		when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

		// when
		CardInfo foundCardInfo = cardInformationService.findByCardInfo(paymentId);

		// then
		verify(paymentRepository).findById(paymentId);
		assertEquals(cardInformation.getCardHolderName(), foundCardInfo.cardHolderName());
		assertEquals(cardInformation.getCardNumber(), foundCardInfo.cardNumber());
		assertEquals(cardInformation.getCardExpiry(), foundCardInfo.expiryDate());
		assertEquals(cardInformation.getCardCvv(), foundCardInfo.cvv());

	}
}
