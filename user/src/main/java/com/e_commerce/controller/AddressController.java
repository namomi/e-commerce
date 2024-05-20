package com.e_commerce.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.e_commerce.dto.AddressInfo;
import com.e_commerce.entity.Address;
import com.e_commerce.service.AddressService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@Tag(name = "주소 API", description = "주소를 등록 및 조회와 삭제가 가능합니다.")
public class AddressController {

	private final AddressService addressService;

	@PostMapping("/register/{userId}/address")
	@Operation(summary = "register address", description = "주소를 등록합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "성공"),
		@ApiResponse(responseCode = "403", description = "Duplicate address."),
	})
	public void registerAddress(@PathVariable
						@Positive(message = "userId는 필수입니다.")
						@Schema(description = "employeeId", example = "2")
						Long userId,
						@Positive(message = "주소 정보를 입력합니다.")
						@RequestBody AddressInfo addressInfo) {
		addressService.addAddress(userId, Address.toEntity(addressInfo));
	}

	@GetMapping("/address/{addressId}")
	@Operation(summary = "find address", description = "주소를 찾습니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "성공"),
		@ApiResponse(responseCode = "403", description = "No matching addresses."),
	})
	public AddressInfo findAddress(@PathVariable
						@Positive(message = "userId는 필수입니다.")
						@Schema(description = "employeeId", example = "2")
						Long addressId) {
		return addressService.findAddressById(addressId);
	}

	@DeleteMapping("/address/{addressId}")
	@Operation(summary = "delete address", description = "주소를 삭제합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "성공"),
		@ApiResponse(responseCode = "403", description = "No matching addresses."),
	})
	public void deleteAddress(@PathVariable
						@Positive(message = "userId는 필수입니다.")
						@Schema(description = "employeeId", example = "2")
						Long addressId) {
		addressService.deleteAddress(addressId);
	}
}
