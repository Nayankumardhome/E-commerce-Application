package com.nayan.projects.eshoppingcart.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nayan.projects.eshoppingcart.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{

	public List<Product> findByIsActiveTrue();

	public List<Product> findByCategoryAndIsActiveTrue(String category);

	public List<Product> findByCategoryAndPriceBetween(String category, double minPrice, double maxPrice);

	public Page<Product> findByIsActiveTrue(Pageable pageable);

	public Page<Product> findByCategoryAndIsActiveTrue(String category, Pageable pageable);

}
