package com.nayan.projects.eshoppingcart.service;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.nayan.projects.eshoppingcart.model.Category;

public interface CategoryService {

	public Category saveCategory(Category category, MultipartFile file) throws IOException;
	
	public Category updateCategory(Category category) throws IOException;
	
	public boolean isExistCategory(String name);
	
	public List<Category> getAllCategory();
	
	public Boolean deleteCategory(int id);
	
	public Category getCategory(int id);
	
	public List<Category> getAllActiveCategory();
	
	public Page<Category> getAllCategoryPagination(Integer pageNo, Integer pageSize);
}
