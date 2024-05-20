package com.e_commerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.e_commerce.entity.FailedEvent;

public interface FailedEventRepository extends JpaRepository<FailedEvent, Long> {
}
