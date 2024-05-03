package com.e_commerce.controller;

import static com.e_commerce.exception.ErrorCode.*;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.e_commerce.dto.AddressInfo;
import com.e_commerce.entity.Address;
import com.e_commerce.exception.CustomException;
import com.e_commerce.service.AddressService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class AddressController {

	private final AddressService addressService;

	@PostMapping("/register/address")
	public void registerAddress(@RequestBody AddressInfo addressInfo) {
		addressService.addAddress(Address.toEntity(addressInfo));
	}

	@GetMapping("/address/{addressId}")
	public Address findAddress(@PathVariable Long addressId) {
		return addressService.findAddressById(addressId)
			.orElseThrow(() -> new CustomException(NO_MATCHING_ADDRESS));
	}

	@DeleteMapping("/address/{addressId}")
	public void deleteAddress(@PathVariable Long addressId) {
		addressService.deleteAddress(addressId);
	}
}
