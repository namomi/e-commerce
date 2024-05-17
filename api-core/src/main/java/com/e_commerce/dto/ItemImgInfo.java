package com.e_commerce.dto;

import org.modelmapper.ModelMapper;

import com.e_commerce.entity.ItemImg;

public record ItemImgInfo(String imgName, String oriImgName, String imgUrl, String repimgYn) {
	private static ModelMapper modelMapper = new ModelMapper();

	public static ItemImgInfo of(ItemImg itemImg) {
		return modelMapper.map(itemImg, ItemImgInfo.class);
	}
}
