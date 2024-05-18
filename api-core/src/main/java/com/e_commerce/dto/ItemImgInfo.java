package com.e_commerce.dto;

import com.e_commerce.entity.ItemImg;
import org.modelmapper.ModelMapper;

public record ItemImgInfo(String imgName, String oriImgName, String imgUrl,
                          String repimgYn) {
    private static ModelMapper modelMapper;

    public static ItemImgInfo of(ItemImg itemImg) {
        return modelMapper.map(itemImg, ItemImgInfo.class);
    }
}
