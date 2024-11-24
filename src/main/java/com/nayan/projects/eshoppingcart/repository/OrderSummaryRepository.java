package com.nayan.projects.eshoppingcart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nayan.projects.eshoppingcart.model.OrderSummary;

public interface OrderSummaryRepository extends JpaRepository<OrderSummary, Integer> {

}
