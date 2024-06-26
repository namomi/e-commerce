package com.e_commerce.dto;

import com.e_commerce.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemInfo {

    private String itemNm;  // 상품명
    private int count; // 주문 수량
    private int orderPrice; //주문 금액
    private String imgUrl;  // 상품 이미지 경로

    public OrderItemInfo(OrderItem orderItem, String imgUrl) {
        this.itemNm = orderItem.getItem().getItemNm();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.imgUrl = imgUrl;
    }
}
