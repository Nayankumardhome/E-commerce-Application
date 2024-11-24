package com.nayan.projects.eshoppingcart.service;


import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.nayan.projects.eshoppingcart.model.Product;


public interface ProductService {

	public Product saveProduct(Product product, MultipartFile file) throws IOException;
	
	public List<Product> getAllProduct();
	
	public Boolean deleteProduct(int id);

	public Product getProduct(int id);

	public Product updateProduct(Product updateProduct) throws IOException;
	
	public List<Product> getAllActiveProduct(String category);

	public List<Product> findReplacementProducts(Integer orderId);

	public List<Product> searchProduct(String querie);

	List<Product> searchAllProducts(String query);
	
	public Page<Product> getAllActiveProductPagination(Integer pageNo, Integer pageSize, String category);

	public Page<Product> getProductPagination(Integer pageNo, Integer pageSize);

	public Page<Product> searchProductPagination(String query, Integer pageNo, Integer pageSize);

	public Page<Product> findReplacementProductsPagination(Integer orderId, Integer pageNo, Integer pageSize);
}
