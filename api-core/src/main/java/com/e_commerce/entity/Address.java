package com.e_commerce.entity;

import com.e_commerce.dto.AddressInfo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Address extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String country;

	@Column(nullable = false)
	private String city;

	@Column(nullable = false)
	private String zipCode;

	@Column(nullable = false)
	private String street;

	public static Address toEntity(AddressInfo addressInfo) {
		return Address.builder()
			.country(addressInfo.country())
			.city(addressInfo.city())
			.zipCode(addressInfo.zipCode())
			.street(addressInfo.street())
			.build();
	}
}
