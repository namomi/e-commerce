package com.e_commerce.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.e_commerce.entity.ItemImg;
import com.e_commerce.exception.CustomException;
import com.e_commerce.repository.ItemImgRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;

import static com.e_commerce.exception.ErrorCode.UPLOAD_FILE_IS_EMPTY;

@Service
public class ItemImgService {

    private final AmazonS3 amazonS3;
    private final String bucketName;
    private final ItemImgRepository itemImgRepository;

    public ItemImgService(AmazonS3 amazonS3, @Value("${cloud.aws.s3.bucket}") String bucketName,
                          ItemImgRepository itemImgRepository) {
        this.amazonS3 = amazonS3;
        this.bucketName = bucketName;
        this.itemImgRepository = itemImgRepository;
    }

    public String uploadFile(Long itemImgId, MultipartFile itemImgFile) throws IOException {
        if (itemImgFile.isEmpty()) {
            throw new CustomException(UPLOAD_FILE_IS_EMPTY);
        }

        String originalFileName = itemImgFile.getOriginalFilename();
        String uniqueFileName = UUID.randomUUID().toString() + "_" + originalFileName;
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(itemImgFile.getSize());
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(itemImgFile.getBytes());

        try {
            amazonS3.putObject(new PutObjectRequest(bucketName, uniqueFileName, byteArrayInputStream, metadata));
        } finally {
            byteArrayInputStream.close();
        }

        // URL을 직접 구성합니다. 예를 들어:
        String imgPath = "https://" + bucketName + ".s3.amazonaws.com/" + uniqueFileName; // 버킷의 구성에 따라 이 URL 패턴을 조정해야 할 수 있습니다.
        ItemImg itemImg = getItemImg(itemImgId, uniqueFileName, originalFileName, imgPath);
        itemImgRepository.save(itemImg);
        return imgPath;
    }

    private ItemImg getItemImg(Long itemImgId, String imgName, String originalFileName, String imgUrl) {
        ItemImg itemImg = itemImgRepository.findById(itemImgId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid item image ID: " + itemImgId));
        itemImg.updateItemImg(originalFileName, imgName, imgUrl);
        return itemImg;
    }

    public void deleteFile(String fileName) {
        amazonS3.deleteObject(bucketName, fileName);
    }

    @Transactional
    public void saveItemImg(ItemImg itemImg, MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            String imgPath = uploadFile(itemImg.getId(), file);
            String filename = file.getOriginalFilename();
            String imgName = imgPath.substring(imgPath.lastIndexOf("/") + 1);
            itemImg.setItemImg(imgName, filename, imgPath);
        }
        itemImgRepository.save(itemImg);
    }
}
