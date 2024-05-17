package com.e_commerce.service;

import static com.e_commerce.exception.ErrorCode.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.e_commerce.dto.ItemImgInfo;
import com.e_commerce.dto.ItemInfo;
import com.e_commerce.entity.Item;
import com.e_commerce.entity.ItemImg;
import com.e_commerce.exception.CustomException;
import com.e_commerce.repository.ItemImgRepository;
import com.e_commerce.repository.ItemRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ItemService {

	private final ItemRepository itemRepository;

	private final ItemImgRepository itemImgRepository;

	private final ItemImgService itemImgService;

	@Transactional
	public void saveItem(ItemInfo itemInfo, List<MultipartFile> itemImgFileList) throws IOException {
		Item item = itemInfo.createItem();
		itemRepository.save(item);

		for (int i = 0; i < itemImgFileList.size(); i++) {
			ItemImg itemImg = new ItemImg();
			itemImg.setItemAndRepimgYn(item, i);
			itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
		}
	}

	@Transactional(readOnly = true)
	public ItemInfo getItemInfo(Long itemId) {
		List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
		List<ItemImgInfo> itemImgInfoList = new ArrayList<>();
		for (ItemImg itemImg : itemImgList) {
			ItemImgInfo itemImgInfo = ItemImgInfo.of(itemImg);
			itemImgInfoList.add(itemImgInfo);
		}

		Item item = itemRepository.findById(itemId)
			.orElseThrow(() -> new CustomException(NO_PRODUCT_INFORMATION));
		ItemInfo itemInfo = ItemInfo.of(item);
		itemInfo.setItemImgInfoList(itemImgInfoList);
		return itemInfo;
	}

	@Transactional
	public void updateItem(ItemInfo itemInfo, List<MultipartFile> itemImgFileList) throws IOException {
		Item item = itemRepository.findById(itemInfo.getId())
			.orElseThrow(() -> new CustomException(NO_PRODUCT_INFORMATION));
		item.updateItem(itemInfo);

		List<Long> itemImgIds = itemInfo.getItemImgIds();
		for (int i = 0; i < itemImgFileList.size(); i++) {
			itemImgService.uploadFile(itemImgIds.get(i), itemImgFileList.get(i));
		}
	}

}
