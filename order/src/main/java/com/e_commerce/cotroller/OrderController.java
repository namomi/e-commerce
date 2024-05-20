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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@Tag(name = "상품 주문 API", description = "상품을 주문 및 주문이력, 취소할수 있습니다.")
public class OrderController {

	private final OrderService orderService;

	@PostMapping("/order")
	@Operation(summary = "order", description = "상품을 주문합니다..")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "성공"),
		@ApiResponse(responseCode = "403", description = "No matching item."),
	})
	public void order(@RequestBody
	@Positive(message = "주문정보를 입력해주세요")
	OrderInfo orderInfo,
		@Positive(message = "유저명 필수입니다.")
		@Schema(description = "username", example = "kim")
		@RequestParam String username,
		@RequestParam(required = false)
		@Schema(description = "couponId", example = "2")
		Long couponId) {
		orderService.order(orderInfo, username, couponId);
	}

	@GetMapping(value = {"/orders", "/orders/{page}"})
	@Operation(summary = "order hist", description = "주문이력을 조회합니다..")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "성공"),
	})
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
	@Operation(summary = "cancel order", description = "주문을 취소합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "성공"),
		@ApiResponse(responseCode = "403", description = "You do not have permission to cancel your order."),
	})
	public void cancelOrder(@PathVariable("orderId")
	@Positive(message = "user id는 필수입니다.")
	@Schema(description = "userId", example = "1")
	Long orderId,
		Principal principal) {
		if (!orderService.validateOrder(orderId, principal.getName())) {
			throw new CustomException(NO_REVOCATION_OF_ORDER);
		}
		orderService.cancelOrder(orderId);
	}
}
