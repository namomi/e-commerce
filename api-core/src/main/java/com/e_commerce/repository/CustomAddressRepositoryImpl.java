package com.e_commerce.repository;

import com.e_commerce.entity.Address;
import com.e_commerce.entity.QAddress;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomAddressRepositoryImpl implements CustomAddressRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public boolean existsAddress(Address address) {
		QAddress qAddress = QAddress.address;
		long count = queryFactory
			.selectFrom(qAddress)
			.where(
				qAddress.country.eq(address.getCountry()),
				qAddress.city.eq(address.getCity()),
				qAddress.zipCode.eq(address.getZipCode()),
				qAddress.street.eq(address.getStreet())
			)
			.fetchCount();
		return count > 0;
	}
}
