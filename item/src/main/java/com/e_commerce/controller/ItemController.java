package com.e_commerce.controller;

import static com.e_commerce.exception.ErrorCode.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.e_commerce.dto.ItemInfo;
import com.e_commerce.dto.ItemSearchDto;
import com.e_commerce.entity.Item;
import com.e_commerce.exception.CustomException;
import com.e_commerce.service.ItemSearchService;
import com.e_commerce.service.ItemService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@Tag(name = "상품 API", description = "상품 등록 및 상세조회 및 수정, 검색이 가능합니다.")
public class ItemController {

	private final ItemService itemService;

	private final ItemSearchService itemSearchService;

	@PostMapping("/items/new")
	@Operation(summary = "create item", description = "상품 등록합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "성공"),
		@ApiResponse(responseCode = "403", description = "An error occurred while registering the product."),
	})
	public void createItem(@ModelAttribute
	@Positive(message = "상품정보를 입력합니다.")
	ItemInfo itemInfo,
		@RequestParam
		@Positive(message = "상품이지미를 등록합니다..")
		List<MultipartFile> itemImgFileList) {
		try {
			itemService.saveItem(itemInfo, itemImgFileList);
			itemService.saveItem(itemInfo, itemImgFileList);
		} catch (Exception e) {
			throw new CustomException(PRODUCT_REGISTRATION_FAILED);
		}
	}

	@GetMapping("/items/{itemId}")
	@Operation(summary = "get item details", description = "상품 상세설명을 가져옵니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "성공"),
		@ApiResponse(responseCode = "403", description = "There is no product information."),
	})
	public ItemInfo getItemDetails(@PathVariable
	@Positive(message = "item id는 필수입니다.")
	@Schema(description = "itemId", example = "1")
	Long itemId) {
		return itemService.getItemInfo(itemId);
	}

	@PostMapping("/items/update")
	@Operation(summary = "update item", description = "상품 정보를 수정합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "성공"),
		@ApiResponse(responseCode = "403", description = "Error updating item."),
	})
	public void updateItem(@ModelAttribute
	@Positive(message = "상품정보를 입력합니다.")
	ItemInfo itemInfo,
		@Positive(message = "상품정보 이미지가 필요합니다.")
		@RequestParam List<MultipartFile> itemImgFileList) {
		try {
			itemService.updateItem(itemInfo, itemImgFileList);
		} catch (IOException e) {
			throw new CustomException(ERROR_UPDATE_ITEM);
		}
	}

	@GetMapping("/items")
	@Operation(summary = "search item", description = "검색 조건에 맞는 상품을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "성공"),
		@ApiResponse(responseCode = "403", description = "Failed to parse search results."),
	})
	public List<ItemInfo> searchItems(
		@RequestBody @Positive(message = "상품 검색정보를 입력합니다..")
		ItemSearchDto itemSearchDto) {
		List<Item> items = itemSearchService.searchItems(itemSearchDto);
		return items.stream().map(ItemInfo::of).collect(Collectors.toList());
	}
}
