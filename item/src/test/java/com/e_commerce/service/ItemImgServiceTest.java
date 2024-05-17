package com.e_commerce.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.mock.web.MockMultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.e_commerce.ApiApplication;
import com.e_commerce.entity.ItemImg;
import com.e_commerce.repository.ItemImgRepository;

@SpringBootTest(classes = ApiApplication.class)
class ItemImgServiceTest {

	@Mock
	private AmazonS3 amazonS3;

	@Mock
	private ItemImgRepository itemImgRepository;

	@InjectMocks
	private ItemImgService itemImgService;

	@MockBean // 여기를 @Mock에서 @MockBean으로 변경
	private Environment env;

	@BeforeEach
	void setUpd() {
		MockitoAnnotations.openMocks(this);
		when(env.getProperty("cloud.aws.s3.bucket")).thenReturn("rerink");
	}

	@Test
	void test() throws IOException {
		//given
		Long itemImgId = 1L;
		String bucketName = "rerink";
		String fixedUUID = "38e74521-b5af-44fd-b34b-997a3d8fdc09";
		MockMultipartFile file = new MockMultipartFile("file", "filename.png", "image/png", "file content".getBytes());
		String expectedUrl = "https://" + bucketName + ".s3.amazonaws.com/" + fixedUUID + "_filename.png";
		ItemImg mockedItemImg = new ItemImg();

		when(amazonS3.putObject(any(PutObjectRequest.class))).thenReturn(new PutObjectResult());
		when(amazonS3.getUrl(eq(bucketName), anyString())).thenAnswer(invocation -> new URL(
			"https://" + bucketName + ".s3.amazonaws.com/" + invocation.getArgument(1, String.class)));

		when(itemImgRepository.findById(eq(itemImgId))).thenReturn(Optional.of(mockedItemImg));

		// When
		String result = itemImgService.uploadFile(itemImgId, file);

		// Then
		assertNotNull(result);
		assertEquals(expectedUrl, result);
		verify(itemImgRepository).save(any(ItemImg.class));
		verify(amazonS3).putObject(any(PutObjectRequest.class));
	}
}
