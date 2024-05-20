package com.e_commerce.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class OrderItem extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "item_id")
	private Item item;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "order_id")
	private Order order;

	private int orderPrice;

	private int count;

	private int discountedPrice;

	public OrderItem(Item item, int orderPrice, int count) {
		this.item = item;
		this.orderPrice = orderPrice;
		this.count = count;
		this.discountedPrice = orderPrice;
	}

	public static OrderItem createItem(Item item, int count, Coupon coupon) {
		OrderItem orderItem = new OrderItem(item, item.getPrice(), count);
		orderItem.applyCoupon(coupon);
		item.removeStock(count);
		return orderItem;
	}

	public int getTotalPrice() {
		return orderPrice * count;
	}

	public void cancel() {
		this.getItem().addStock(count);
	}

	public void applyCoupon(Coupon coupon) {
		if (coupon != null && coupon.isIssued()) {
			double discountRate = coupon.getDiscountRate();
			this.discountedPrice = (int)(orderPrice * (1 - discountRate / 100.0));
		}
	}
}
