package com.e_commerce.repository;

import com.e_commerce.entity.Address;

public interface CustomAddressRepository {
    boolean existsAddress(Address address);
}
