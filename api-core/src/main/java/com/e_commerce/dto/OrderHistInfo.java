package com.e_commerce.dto;

import com.e_commerce.constant.OrderStatus;
import com.e_commerce.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderHistInfo {

    private Long orderId;

    private String orderDate;

    private OrderStatus orderStatus;

    private List<OrderItemInfo> orderItemInfoList = new ArrayList<>();

    public OrderHistInfo(Order order) {
        this.orderId = order.getId();
        this.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.orderStatus = order.getOrderStatus();
    }

    public void addOrderItemInfo(OrderItemInfo orderItemInfo) {
        orderItemInfoList.add(orderItemInfo);
    }
}
