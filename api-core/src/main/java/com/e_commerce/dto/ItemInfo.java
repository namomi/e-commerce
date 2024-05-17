package com.e_commerce.dto;

import java.util.List;

import org.modelmapper.ModelMapper;

import com.e_commerce.constant.ItemSellStatus;
import com.e_commerce.constant.ProductStatus;
import com.e_commerce.entity.Item;

import lombok.Getter;

@Getter
public class ItemInfo {

	private Long id;

	private String itemNm;

	private Integer price;

	private String itemDetail;

	private Integer stockNumber;

	private ProductStatus productStatus;

	private ItemSellStatus itemSellStatus;

	private List<ItemImgInfo> itemImgInfoList;

	private List<Long> itemImgIds;

	private static ModelMapper modelMapper = new ModelMapper();

	public Item createItem() {
		return modelMapper.map(this, Item.class);
	}

	public static ItemInfo of(Item item) {
		return modelMapper.map(item, ItemInfo.class);
	}

	public void setItemImgInfoList(List<ItemImgInfo> itemImgInfoList) {
		this.itemImgInfoList = itemImgInfoList;
	}
}
