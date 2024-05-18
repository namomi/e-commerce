package com.e_commerce.dto;

public record CartDetailInfo(Long cartItemId, String itemNm, int price,
                             int count,
                             String imgUrl) {
    public CartDetailInfo(Long cartItemId, String itemNm, int price, int count, String imgUrl) {
        this.cartItemId = cartItemId;
        this.itemNm = itemNm;
        this.price = price;
        this.count = count;
        this.imgUrl = imgUrl;
    }
}
