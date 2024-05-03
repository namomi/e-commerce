package com.e_commerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.e_commerce.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long>, CustomAddressRepository {

}
