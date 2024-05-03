package com.e_commerce.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.e_commerce.entity.Address;
import com.e_commerce.repository.AddressRepository;

class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressService addressService;

    private Address address;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        address = new Address(1L, "대한민국", "서울", "12345", "강남대로");
    }

    @Test
    public void AddAddressSuccessTest() {
        // given
        when(addressRepository.existsAddress(address)).thenReturn(false);

        // when
        addressService.addAddress(address);

        // then
        verify(addressRepository).save(address);
    }

    @Test
    public void findAddressSuccessTest() {
        // given
        Long addressId = address.getId();
        when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));

        // when
        Optional<Address> foundAddress = addressService.findAddressById(addressId);

        // then
        assertTrue(foundAddress.isPresent());
        assertEquals(address, foundAddress.get());
        verify(addressRepository).findById(addressId);
    }

    @Test
    public void deleteSuccessTest() {
        // given
        Long addressId = address.getId();
        when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));

        // when
        addressService.deleteAddress(addressId);

        // then
        verify(addressRepository).delete(address);
    }
}
