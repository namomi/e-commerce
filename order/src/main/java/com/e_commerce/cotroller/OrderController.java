package com.e_commerce.cotroller;

import com.e_commerce.dto.OrderHistInfo;
import com.e_commerce.dto.OrderInfo;
import com.e_commerce.dto.OrderResponse;
import com.e_commerce.exception.CustomException;
import com.e_commerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

import static com.e_commerce.exception.ErrorCode.NO_REVOCATION_OF_ORDER;

@RequiredArgsConstructor
@RestController
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/order")
    public void order(@RequestBody OrderInfo orderInfo, @RequestParam String username) {
        orderService.order(orderInfo, username);
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
