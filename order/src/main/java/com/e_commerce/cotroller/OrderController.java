package com.e_commerce.cotroller;

import static com.e_commerce.exception.ErrorCode.*;

import java.security.Principal;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.e_commerce.dto.OrderHistInfo;
import com.e_commerce.dto.OrderInfo;
import com.e_commerce.dto.OrderResponse;
import com.e_commerce.exception.CustomException;
import com.e_commerce.service.OrderService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class OrderController {

	private final OrderService orderService;

	@PostMapping("/order")
	public void order(@RequestBody OrderInfo orderInfo, @RequestParam String username,
		@RequestParam(required = false) Long couponId) {
		orderService.order(orderInfo, username, couponId);
	}

	@GetMapping(value = {"/orders", "/orders/{page}"})
	public ResponseEntity<OrderResponse> orderHist(
		@PathVariable("page") Optional<Integer> page,
		Principal principal) {
		PageRequest pageable = PageRequest.of(page.orElse(0), 4);
		Page<OrderHistInfo> orderHistInfoList = orderService.getOrderList(principal.getName(), pageable);

		OrderResponse orderResponse = new OrderResponse(
			orderHistInfoList,
			pageable.getPageNumber(),
			5
		);

		return ResponseEntity.ok(orderResponse);
	}

	@PostMapping("/order/{orderId}/cancel")
	public void cancelOrder(@PathVariable("orderId") Long orderId, Principal principal) {
		if (!orderService.validateOrder(orderId, principal.getName())) {
			throw new CustomException(NO_REVOCATION_OF_ORDER);
		}
		orderService.cancelOrder(orderId);
	}
}
