package com.e_commerce.entity;

import com.e_commerce.constant.ItemSellStatus;
import com.e_commerce.constant.ProductStatus;
import com.e_commerce.dto.ItemInfo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;    // 상품 코드

	@Column(nullable = false, length = 50)
	private String itemNm;  // 상품명

	@Column(name = "price", nullable = false)
	private int price;  //가격

	@Column(nullable = false)
	private int stockNumber;    // 재고수량

	@Lob
	@Column(nullable = false)
	private String itemDetail;  // 상품 상세 설명

	@Enumerated(EnumType.STRING)
	private ProductStatus productStatus; // 상품 신제품 인지 중고인지

	@Enumerated(EnumType.STRING)
	private ItemSellStatus itemSellStatus;  //  상품 판매 상태

	public void updateItem(ItemInfo itemInfo) {
		this.itemNm = itemInfo.getItemNm();
		this.price = itemInfo.getPrice();
		this.stockNumber = itemInfo.getStockNumber();
		this.itemDetail = itemInfo.getItemDetail();
		this.productStatus = itemInfo.getProductStatus();
		this.itemSellStatus = itemInfo.getItemSellStatus();
	}
}
