package com.e_commerce.service;

import static com.e_commerce.exception.ErrorCode.*;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.e_commerce.entity.Address;
import com.e_commerce.entity.User;
import com.e_commerce.exception.CustomException;
import com.e_commerce.repository.AddressRepository;
import com.e_commerce.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AddressService {

	private final UserRepository userRepository;

	private final AddressRepository addressRepository;

	@Transactional
	public void addAddress(Long userId, Address address) {
		if (!validateAddress(address)) {
			saveAddress(userId, address);
		} else
			throw new CustomException(DUPLICATE_ADDRESS);
	}

	public Optional<Address> findAddressById(Long addressId) {
		return addressRepository.findById(addressId);
	}

	@Transactional
	public void deleteAddress(Long addressId) {
		Address address = getAddress(addressId);

		addressRepository.delete(address);
	}

	private void saveAddress(Long userId, Address address) {
		User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(NO_MATCHING_USER));
		user.addAddress(address);
		addressRepository.save(address);
	}

	private Address getAddress(Long addressId) {
		Address address = findAddressById(addressId)
			.orElseThrow(() -> new CustomException(NO_MATCHING_ADDRESS));
		return address;
	}

	private boolean validateAddress(Address address) {
		return addressRepository.existsAddress(address);
	}

}
