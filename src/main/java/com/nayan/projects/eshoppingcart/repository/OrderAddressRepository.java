package com.nayan.projects.eshoppingcart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nayan.projects.eshoppingcart.model.OrderAddress;

public interface OrderAddressRepository extends JpaRepository<OrderAddress, Integer>{

}
