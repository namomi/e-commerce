package com.e_commerce.dto;

import com.e_commerce.constant.ItemSellStatus;

import lombok.Getter;

@Getter
public class ItemSearchDto {

	private String searchDateType; // 상품 등록일

	private ItemSellStatus itemSellStatus; // 판매 상태

	private String searchBy; // 어떤 유형으로 조회 할지

	private String searchQuery = "";
}
