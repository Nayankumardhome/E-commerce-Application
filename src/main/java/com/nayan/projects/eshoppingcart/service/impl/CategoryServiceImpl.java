package com.nayan.projects.eshoppingcart.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.nayan.projects.eshoppingcart.model.Category;
import com.nayan.projects.eshoppingcart.model.Product;
import com.nayan.projects.eshoppingcart.repository.CategoryRepository;
import com.nayan.projects.eshoppingcart.service.CategoryService;


@Service
public class CategoryServiceImpl implements CategoryService{
	
	@Autowired
	private CategoryRepository categoryRepository;
	

	@Override
	public Category saveCategory(Category category, MultipartFile file) throws IOException {
		
			File saveFile = new ClassPathResource("/static/img").getFile();
			
			if(!saveFile.exists()) {
				saveFile.mkdirs();
			}
			
			Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "category_img" 
						+ File.separator + file.getOriginalFilename());
			
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			
			
		return categoryRepository.save(category);
	}


	@Override
	public boolean isExistCategory(String name) {
		return categoryRepository.existsByName(name);
	}

	@Override
	public List<Category> getAllCategory() {
		return categoryRepository.findAll();
	}

	@Override
	public Boolean deleteCategory(int id) {
		
		Category category = categoryRepository.findById(id).orElse(null);
		
		if(!ObjectUtils.isEmpty(category)) {
			categoryRepository.delete(category);
			return true;
		}
		
		return false;
	}

	@Override
	public Category getCategory(int id) {		
		return categoryRepository.findById(id).orElse(null);
	}

	@Override
	public Category updateCategory(Category category) throws IOException {
	    return categoryRepository.save(category);
	}

	@Override
	public List<Category> getAllActiveCategory() {
		return categoryRepository.findByIsActiveTrue();
	}

	@Override
	public Page<Category> getAllCategoryPagination(Integer pageNo, Integer pageSize) {
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		return categoryRepository.findAll(pageable);
	}


}
