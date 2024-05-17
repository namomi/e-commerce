package com.e_commerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.e_commerce.entity.ItemImg;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {

	List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);
}
