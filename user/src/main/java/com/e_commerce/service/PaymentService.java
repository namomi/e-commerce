package com.e_commerce.service;

import static com.e_commerce.constant.PaymentType.*;
import static com.e_commerce.constant.Role.*;
import static com.e_commerce.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.e_commerce.entity.Payment;
import com.e_commerce.entity.User;
import com.e_commerce.exception.CustomException;
import com.e_commerce.repository.PaymentRepository;
import com.e_commerce.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PaymentService {

	private final UserRepository userRepository;

	private final PaymentRepository paymentRepository;

	@Transactional
	public String addPayment(Long userId, Payment payment) {
		Payment savedPayment = savedPayment(userId, payment);
		if (savedPayment.getPaymentType() == CARD) {
			return "redirect:/" + savedPayment.getId() + "/card";
		}
		throw new CustomException(MISSING_INFORMATION);
	}

	public Payment findByPayment(Long userId) {
		User user = getUser(userId);
		return user.getPayment();
	}

	@Transactional
	public void deletePayment(Long userId) {
		User user = getUser(userId);
		user.addPayment(null);
	}

	private Payment savedPayment(Long userId, Payment payment) {
		User user = getUser(userId);
		validatePayment(user);
		user.addPayment(payment);

		return paymentRepository.save(payment);
	}

	private User getUser(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(NO_MATCHING_USER));
		authorizationVerification(user);
		return user;
	}

	private void authorizationVerification(User user) {
		if (user.getRole() != USER)
			throw new CustomException(NOT_AUTHORIZED);
	}

	private void validatePayment(User user) {
		if (user.getPayment() != null)
			throw new CustomException(DUPLICATE_PAYMENT);
	}
}
