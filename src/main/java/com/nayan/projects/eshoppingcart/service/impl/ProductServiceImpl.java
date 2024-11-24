package com.nayan.projects.eshoppingcart.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.nayan.projects.eshoppingcart.model.Product;
import com.nayan.projects.eshoppingcart.model.ProductOrder;
import com.nayan.projects.eshoppingcart.repository.ProductOrderRepository;
import com.nayan.projects.eshoppingcart.repository.ProductRepository;
import com.nayan.projects.eshoppingcart.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService{

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ProductOrderRepository productOrderRepository;
	
	@Override
	public Product saveProduct(Product product, MultipartFile file) throws IOException {
		
		File saveFile = new ClassPathResource("/static/img").getFile();
		
		if(!saveFile.exists()) {
			saveFile.mkdirs();
		}
		
		Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "product_img" 
					+ File.separator + file.getOriginalFilename());
		
		System.out.println(path);
		try {
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
		
		return productRepository.save(product);
	}

	@Override
	public List<Product> getAllProduct() {
		return productRepository.findAll();
	}

	@Override
	public Boolean deleteProduct(int id) {
		
		Product oldProduct = productRepository.findById(id).orElse(null);
		
		if(!ObjectUtils.isEmpty(oldProduct)) {
			productRepository.delete(oldProduct);
			return true;
		}
		
		return false;
	}

	@Override
	public Product getProduct(int id) {
		return productRepository.findById(id).orElse(null);
	}

	@Override
	public Product updateProduct(Product updateProduct) throws IOException{
		return productRepository.save(updateProduct);
	}

	@Override
	public List<Product> getAllActiveProduct(String category) {
		List<Product> products = null;
		if(ObjectUtils.isEmpty(category)) {
			products = productRepository.findByIsActiveTrue();
		} else {
			products = productRepository.findByCategoryAndIsActiveTrue(category);
		}
		return products;
	}

	@Override
	public List<Product> findReplacementProducts(Integer orderId) {
		
		ProductOrder originalOrder = productOrderRepository.findById(orderId).orElse(null);
		
		if(originalOrder == null) {
			return List.of();
		}
		
		Product originalProduct = originalOrder.getProduct();
		Double originalPrice = originalProduct.getPrice();
		String category = originalProduct.getCategory();
		
        double minPrice = originalPrice - 100;
        double maxPrice = originalPrice + 100;
        
        List<Product> replacementProducts = productRepository.findByCategoryAndPriceBetween(category, minPrice, maxPrice);
		
		return replacementProducts;
	}
	
	@Override
	public Page<Product> findReplacementProductsPagination(Integer orderId, Integer pageNo, Integer pageSize) {
		ProductOrder originalOrder = productOrderRepository.findById(orderId).orElse(null);
		
		if(originalOrder == null) {
			List<Product> products = List.of();
			Pageable pageable = PageRequest.of(pageNo, pageSize);
			int start = Math.min((int) pageable.getOffset(), products.size());
		    int end = Math.min((start + pageable.getPageSize()), products.size());
		    List<Product> pagedProducts = products.subList(start, end);
			return new PageImpl<Product>(pagedProducts, pageable, products.size());
		}
		
		Product originalProduct = originalOrder.getProduct();
		Double originalPrice = originalProduct.getPrice();
		String category = originalProduct.getCategory();
		
        double minPrice = originalPrice - 100;
        double maxPrice = originalPrice + 100;
        
        List<Product> replacementProducts = productRepository.findByCategoryAndPriceBetween(category, minPrice, maxPrice);
        Pageable pageable = PageRequest.of(pageNo, pageSize);
	    int start = Math.min((int) pageable.getOffset(), replacementProducts.size());
	    int end = Math.min((start + pageable.getPageSize()), replacementProducts.size());
	    List<Product> pagedProducts = replacementProducts.subList(start, end);
		
		return new PageImpl<Product>(pagedProducts, pageable, replacementProducts.size());
	}

	@Override
	public List<Product> searchProduct(String query) {
		 return productRepository.findAll().stream()
	                .filter(product -> product.getIsActive() &&
	                        (product.getProductName().toLowerCase().contains(query.toLowerCase()) ||
	                         product.getCategory().toLowerCase().contains(query.toLowerCase())))
	                .collect(Collectors.toList());	
	}
	
	@Override
	public Page<Product> searchProductPagination(String query, Integer pageNo, Integer pageSize) {
		
		List<Product> filteredProducts = productRepository.findAll().stream()
		        .filter(product -> product.getIsActive() &&
		                (product.getProductName().toLowerCase().contains(query.toLowerCase()) ||
		                 product.getCategory().toLowerCase().contains(query.toLowerCase())))
		        .collect(Collectors.toList());

		    Pageable pageable = PageRequest.of(pageNo, pageSize);

		    int start = Math.min((int) pageable.getOffset(), filteredProducts.size());
		    int end = Math.min((start + pageable.getPageSize()), filteredProducts.size());
		    List<Product> pagedProducts = filteredProducts.subList(start, end);

		    return new PageImpl<>(pagedProducts, pageable, filteredProducts.size());
	}

	@Override
	public List<Product> searchAllProducts(String query) {
		 return productRepository.findAll().stream()
	                .filter(product ->
	                        (product.getProductName().toLowerCase().contains(query.toLowerCase()) ||
	                         product.getCategory().toLowerCase().contains(query.toLowerCase())))
	                .collect(Collectors.toList());	
	}

	@Override
	public Page<Product> getAllActiveProductPagination(Integer pageNo, Integer pageSize, String category) {
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		Page<Product> pageProduct = null;
		if(ObjectUtils.isEmpty(category)) {
			pageProduct = productRepository.findByIsActiveTrue(pageable);
		} else {
			pageProduct = productRepository.findByCategoryAndIsActiveTrue(category,pageable);
		}
		return pageProduct;
	}

	@Override
	public Page<Product> getProductPagination(Integer pageNo, Integer pageSize) {
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		return productRepository.findAll(pageable);
	}

}
