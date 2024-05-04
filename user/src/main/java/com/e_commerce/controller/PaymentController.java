package com.e_commerce.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.e_commerce.dto.PaymentInfo;
import com.e_commerce.entity.Payment;
import com.e_commerce.service.PaymentService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class PaymentController {

	private final PaymentService paymentService;

	@PostMapping("/register/{userId}/payment")
	public String addPayment(@PathVariable Long userId, @RequestBody PaymentInfo paymentInfo) {
		return paymentService.addPayment(userId, Payment.toEntity(paymentInfo));
	}

	@GetMapping("/{userId}/payment")
	public Payment findByPayment(@PathVariable Long userId) {
		return paymentService.findByPayment(userId);
	}

	@DeleteMapping("{userId}/payment")
	public void deletePayment(@PathVariable Long userId) {
		paymentService.deletePayment(userId);
	}
}
