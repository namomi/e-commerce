package com.e_commerce.controller;

import static com.e_commerce.exception.ErrorCode.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.e_commerce.dto.ItemInfo;
import com.e_commerce.dto.ItemSearchDto;
import com.e_commerce.entity.Item;
import com.e_commerce.exception.CustomException;
import com.e_commerce.service.ItemSearchService;
import com.e_commerce.service.ItemService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class ItemController {

	private final ItemService itemService;

	private final ItemSearchService itemSearchService;

	@PostMapping("/items/new")
	public void createItem(@ModelAttribute ItemInfo itemInfo, @RequestParam List<MultipartFile> itemImgFileList) {
		try {
			itemService.saveItem(itemInfo, itemImgFileList);
			itemService.saveItem(itemInfo, itemImgFileList);
		} catch (Exception e) {
			throw new CustomException(PRODUCT_REGISTRATION_FAILED);
		}
	}

	@GetMapping("/items/{itemId}")
	public ItemInfo getItemDetails(@PathVariable Long itemId) {
		return itemService.getItemInfo(itemId);
	}

	@PostMapping("/items/update")
	public void updateItem(@ModelAttribute ItemInfo itemInfo, @RequestParam List<MultipartFile> itemImgFileList) {
		try {
			itemService.updateItem(itemInfo, itemImgFileList);
		} catch (IOException e) {
			throw new CustomException(ERROR_UPDATE_ITEM);
		}
	}

	@GetMapping("/items")
	public List<ItemInfo> searchItems(ItemSearchDto itemSearchDto) {
		List<Item> items = itemSearchService.searchItems(itemSearchDto);
		return items.stream().map(ItemInfo::of).collect(Collectors.toList());
	}
}
