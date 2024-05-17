package com.e_commerce.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "item_img")
@Getter
public class ItemImg {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String imgName; // 이미지 파일명

	private String oriImgName;  // 원본 이미지 파일명

	private String imgUrl;  // 이미지 조회 경로

	private String repimgYn;  // 대표 이미지 여부

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;

	public void setItemImg(String imgName, String oriImgName, String imgUrl) {
		this.imgName = imgName;
		this.oriImgName = oriImgName;
		this.imgUrl = imgUrl;
	}

	public void setItemAndRepimgYn(Item item, int i) {
		this.item = item;
		this.repimgYn = (i == 0 ? "Y" : "N"); // 첫번째 사진은 대표사진으로 셋팅, 나머지는 상품 이미지
	}

	public void updateItemImg(String oriImgName, String imgName, String imgUrl) {
		this.oriImgName = oriImgName;
		this.imgName = imgName;
		this.imgUrl = imgUrl;
	}
}
