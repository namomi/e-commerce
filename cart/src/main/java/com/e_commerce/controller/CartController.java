package com.e_commerce.controller;

import com.e_commerce.dto.CartDetailInfo;
import com.e_commerce.dto.CartItemInfo;
import com.e_commerce.dto.CartOrderInfo;
import com.e_commerce.exception.CustomException;
import com.e_commerce.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import static com.e_commerce.exception.ErrorCode.*;

@RequiredArgsConstructor
@RestController
public class CartController {

    private final CartService cartService;

    @PostMapping("/cart")
    public void order(@RequestBody CartItemInfo cartItemInfo, Principal principal) {
        String username = principal.getName();

        try {
            cartService.addCart(cartItemInfo, username);
        } catch (Exception e) {
            throw new CustomException(SHOPPING_BASKET_ERROR);
        }
    }

    @GetMapping("/cart")
    public List<CartDetailInfo> orderHist(@RequestParam("cartId") Long cartId) throws IOException {
        return cartService.findCartList(cartId);
    }

    @PostMapping("/cart/orders")
    public void orderCartItem(@RequestBody CartOrderInfo cartOrderInfo, Principal principal) {
        List<CartOrderInfo> cartOrderInfoList = cartOrderInfo.cartOrderInfoList();

        if (cartOrderInfoList == null || cartOrderInfoList.size() == 0) {
            throw new CustomException(NO_PRODUCT_SELECTION);
        }

        for (CartOrderInfo cartOrder : cartOrderInfoList) {
            if (!cartService.validateCartItem(cartOrder.cartItemId(), principal.getName())) {
                throw new CustomException(NOT_AUTHORIZED_TO_ORDER);
            }
        }

        cartService.orderCartItem(cartOrderInfoList, principal.getName());
    }
}
