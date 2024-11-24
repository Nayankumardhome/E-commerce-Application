package com.nayan.projects.eshoppingcart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nayan.projects.eshoppingcart.model.ProductOrder;

public interface ProductOrderRepository extends JpaRepository<ProductOrder, Integer>{

	public List<ProductOrder> findByUserId(Integer userId);

	@Query("SELECT DISTINCT YEAR(p.orderSummary.orderDate) FROM ProductOrder p ORDER BY YEAR(p.orderSummary.orderDate) DESC")
	public List<Integer> findDistinctOrderYears();
	
	public List<ProductOrder> findByProductOrderId(Long productOrderId);
}
