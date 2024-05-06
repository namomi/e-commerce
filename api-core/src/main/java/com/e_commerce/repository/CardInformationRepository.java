package com.e_commerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.e_commerce.entity.CardInformation;

public interface CardInformationRepository extends JpaRepository<CardInformation, Long> {
}
