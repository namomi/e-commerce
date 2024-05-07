package com.e_commerce.service;

import static com.e_commerce.constant.PaymentType.*;
import static com.e_commerce.constant.Role.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.e_commerce.entity.CardInformation;
import com.e_commerce.entity.Payment;
import com.e_commerce.entity.User;
import com.e_commerce.repository.PaymentRepository;
import com.e_commerce.repository.UserRepository;

import e_commerce.service.PaymentService;

class PaymentServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private PaymentRepository paymentRepository;

	@InjectMocks
	private PaymentService paymentService;

	private User user;
	private Payment payment;
	private CardInformation cardInformation;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		cardInformation = new CardInformation(1L, "hana", "1111-2222-3333-4444", "08/28", "018");
		payment = new Payment(1L, cardInformation, CARD);
		// user = new User(1L, "username", "password", "email@example.com", USER, null, null);
		user = mock(User.class);
	}

	@Test
	public void addPaymentSuccessTest() {
		//given
		Long userId = 1L;
		when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
		when(user.getRole()).thenReturn(USER);

		//when
		String savedPayment = paymentService.addPayment(userId, payment);

		//then
		assertNotNull(savedPayment);
		assertTrue(savedPayment.contains("redirect:/1/card"));
		verify(paymentRepository).save(payment);
	}

	@Test
	public void findPaymentSuccessTest() {
		// given
		Long userId = 1L;
		when(user.getPayment()).thenReturn(payment);
		when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		when(user.getRole()).thenReturn(USER);

		// when
		Payment foundPayment = paymentService.findByPayment(userId);

		// then
		verify(userRepository).findById(userId);
		assertNotNull(foundPayment);
		assertEquals(payment, foundPayment);
	}

	@Test
	public void deletePaymentSuccessTest() throws Exception {
		//given
		Long userId = 1L;
		when(user.getPayment()).thenReturn(payment);
		when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		when(user.getRole()).thenReturn(USER);

		//when
		paymentService.deletePayment(userId);

		//then
		verify(paymentRepository).deleteByUserId(userId);
	}
}
