package com.e_commerce.controller;

import static com.e_commerce.exception.ErrorCode.*;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.e_commerce.dto.CartDetailInfo;
import com.e_commerce.dto.CartItemInfo;
import com.e_commerce.dto.CartOrderInfo;
import com.e_commerce.exception.CustomException;
import com.e_commerce.service.CartService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@Tag(name = "장바구니 API", description = "장바구니에 상품을 담으며, 당긴 상품을 주문하며, 조회를 합니다.")
public class CartController {

	private final CartService cartService;

	@Operation(summary = "order", description = "장바구니에 상품을 담습니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "성공"),
		@ApiResponse(responseCode = "404", description = "Can't be stored in your shopping cart."),
	})
	@PostMapping("/cart")
	public void order(@RequestBody
					@Positive(message = "장바구니 상품을 입력해주세요")
					CartItemInfo cartItemInfo,
						Principal principal) {
		String username = principal.getName();

		try {
			cartService.addCart(cartItemInfo, username);
		} catch (Exception e) {
			throw new CustomException(SHOPPING_BASKET_ERROR);
		}
	}

	@Operation(summary = "order hist", description = "주문이력 조회.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "성공"),
	})
	@GetMapping("/cart")
	public List<CartDetailInfo> orderHist(@RequestParam("cartId")
								@Positive(message = "cart id는 필수입니다.")
								@Schema(description = "cartId", example = "2")
	Long cartId) throws IOException {
		return cartService.findCartList(cartId);
	}

	@Operation(summary = "order cartItem", description = "장바구니 상품을 주문합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "성공"),
		@ApiResponse(responseCode = "400", description = "Please select the product to order."),
	})
	@PostMapping("/cart/orders")
	public void orderCartItem(@RequestBody
								@Positive(message = "장바구니 상품을 입력해주세요")
								CartOrderInfo cartOrderInfo,
								Principal principal,
								@RequestParam(required = false)
								@Schema(description = "couponId", example = "2")
								Long couponId) {
		List<CartOrderInfo> cartOrderInfoList = cartOrderInfo.cartOrderInfoList();

		if (cartOrderInfoList == null || cartOrderInfoList.size() == 0) {
			throw new CustomException(NO_PRODUCT_SELECTION);
		}

		for (CartOrderInfo cartOrder : cartOrderInfoList) {
			if (!cartService.validateCartItem(cartOrder.cartItemId(), principal.getName())) {
				throw new CustomException(NOT_AUTHORIZED_TO_ORDER);
			}
		}

		cartService.orderCartItem(cartOrderInfoList, principal.getName(), couponId);
	}
}
