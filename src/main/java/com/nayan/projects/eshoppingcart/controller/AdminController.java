package com.nayan.projects.eshoppingcart.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nayan.projects.eshoppingcart.model.Category;
import com.nayan.projects.eshoppingcart.model.Product;
import com.nayan.projects.eshoppingcart.model.ProductOrder;
import com.nayan.projects.eshoppingcart.model.UserDtls;
import com.nayan.projects.eshoppingcart.service.CartService;
import com.nayan.projects.eshoppingcart.service.CategoryService;
import com.nayan.projects.eshoppingcart.service.OrderService;
import com.nayan.projects.eshoppingcart.service.ProductService;
import com.nayan.projects.eshoppingcart.service.UserService;
import com.nayan.projects.eshoppingcart.util.CommanUtil;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private CommanUtil commanUtil;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@ModelAttribute
	public void getUserDetails(Principal principal, Model model) {
		if(!ObjectUtils.isEmpty(principal)) {
			UserDtls user = userService.getUserByEmail(principal.getName());
			model.addAttribute("user", user);
			model.addAttribute("cardItemsCount", cartService.getCartIntemsCount(user.getId()));model.addAttribute("user", userService.getUserByEmail(principal.getName()));
		}
		model.addAttribute("categories", categoryService.getAllActiveCategory());
	}

	@GetMapping("/")
	public String index() {
		return "admin/index";
	}
	
	@GetMapping("/addProduct")
	public String addProduct() {
		return "admin/add_product";
	}
	
	/*
	 * Category Section
	 */
	
	@GetMapping("/category")
	public String category(Model model,
			@RequestParam(defaultValue = "0") Integer pageNo,
			@RequestParam(defaultValue = "6") Integer pageSize) {
//		model.addAttribute("categories", categoryService.getAllCategory());
		Page<Category> page = categoryService.getAllCategoryPagination(pageNo, pageSize);
		List<Category> categories = page.getContent();
		model.addAttribute("categories", categories);
		model.addAttribute("categoriesSize", categories.size());
		model.addAttribute("pageNo", page.getNumber());
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("totalElements", page.getTotalElements());
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("isFirst", page.isFirst());
		model.addAttribute("isLast", page.isLast());
		
		return "admin/category";
	}
	
	@PostMapping("/saveCategory")
	public String saveCategory(@ModelAttribute Category category, 
            @RequestParam MultipartFile file, HttpSession session) {
		
		String image = (!ObjectUtils.isEmpty(file)) ? file.getOriginalFilename() : "default.jpg";
		
		category.setImage(image);
		
		if(categoryService.isExistCategory(category.getName())) {
			session.setAttribute("errorMsg", "Category Name Already Exist");
		} else {
			Category saveCategory = null;
				try {
					saveCategory = categoryService.saveCategory(category,file);
				} catch (IOException e) {
					session.setAttribute("errorMsg", "Not Save! Internal Server Error\n"+e.getMessage());
				}
				
				if(!ObjectUtils.isEmpty(saveCategory)) {
					session.setAttribute("successMsg", "Saved Successfully.");
				} else {
					session.setAttribute("errorMsg", "Not Save! Something wrong on server side.");
				}
			}
		return "redirect:/admin/category";
	}
	
	@GetMapping("/editCategory/{id}")
	public String editCategory(@PathVariable Integer id, Model model) {
		model.addAttribute("category", categoryService.getCategory(id));
		return "admin/edit_category";
	}
	
	@PostMapping("/updateCategory")
	public String updateCategory(@ModelAttribute Category category, 
            @RequestParam MultipartFile file, HttpSession session) {
		
		Category oldCategory = categoryService.getCategory(category.getId());
	    if (ObjectUtils.isEmpty(oldCategory)) {
	        session.setAttribute("errorMsg", "Category not found!");
	        return "redirect:/admin/category";
	    }

	    oldCategory.setName(category.getName());
	    oldCategory.setIsActive(category.getIsActive());

	    try {
	        if (!file.isEmpty()) {
	            oldCategory.setImage(file.getOriginalFilename());
	            categoryService.saveCategory(oldCategory, file);
	        } else {
	            categoryService.updateCategory(oldCategory);
	        }
	        session.setAttribute("successMsg", "Category updated successfully.");
	    } catch (IOException e) {
	        session.setAttribute("errorMsg", "Category not updated! Internal server error: " + e.getMessage());
	    } catch (Exception e) {
	        session.setAttribute("errorMsg", "Unexpected error occurred: " + e.getMessage());
	    }
		
		return "redirect:/admin/editCategory/" + category.getId();
	}
	
	@GetMapping("/deleteCategory/{id}")
	public String deleteCategory(@PathVariable Integer id, HttpSession session) {
		
		Boolean deleteCategory = categoryService.deleteCategory(id);
		if(!ObjectUtils.isEmpty(deleteCategory)) {
			session.setAttribute("successMsg", "Category deleted Successfully.");
		} else {
			session.setAttribute("errorMsg", "Category Not deleted. Something wrong on server side.");
		}
		
		return "redirect:/admin/category";
	}
	
	/*
	 * Product Section
	 */
	
	@GetMapping("/products")
	public String loadAddProduct(Model model,
			@RequestParam(defaultValue = "0") Integer pageNo,
			@RequestParam(defaultValue = "3") Integer pageSize) {
		model.addAttribute("categories", categoryService.getAllCategory());
//		model.addAttribute("products", productService.getAllProduct());
		
		Page<Product> page = productService.getProductPagination(pageNo, pageSize);
		List<Product> products = page.getContent();
		model.addAttribute("products", products);
		model.addAttribute("productsSize", products.size());
		model.addAttribute("pageNo", page.getNumber());
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("totalElements", page.getTotalElements());
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("isFirst", page.isFirst());
		model.addAttribute("isLast", page.isLast());
		
		return "admin/products";
	}
	
	@GetMapping("/searchProduct")
	public String searchProduct(@RequestParam String query, Model model,
			@RequestParam(defaultValue = "0") Integer pageNo,
			@RequestParam(defaultValue = "3") Integer pageSize) {

		model.addAttribute("categories", categoryService.getAllCategory());
//		List<Product> products = productService.searchProduct(query);
		Page<Product> page = productService.searchProductPagination(query, pageNo, pageSize);
		List<Product> products = page.getContent();
		model.addAttribute("products", products);
		model.addAttribute("productsSize", products.size());
		model.addAttribute("pageNo", page.getNumber());
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("totalElements", page.getTotalElements());
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("isFirst", page.isFirst());
		model.addAttribute("isLast", page.isLast());
		return "admin/products";
	}
	
	@PostMapping("/saveProduct")
	public String saveProduct(@ModelAttribute Product product, 
            @RequestParam MultipartFile file, HttpSession session) {
		
		String image = (!ObjectUtils.isEmpty(file)) ? file.getOriginalFilename() : "default.jpg";
		
		product.setImage(image);
		product.setDiscount(0.0);
		product.setDiscountPrice(product.getPrice());
		
		Product saveProduct = null;
		
		try {
			saveProduct = productService.saveProduct(product, file);
		} catch (IOException e) {
			session.setAttribute("errorMsg", "Product Not Added! Internal server error: " + e.getMessage());
		} catch (Exception e) {
			session.setAttribute("errorMsg", "Unexpected error occurred: " + e.getMessage());
		}
		
		if(ObjectUtils.isEmpty(saveProduct)) {
			session.setAttribute("errorMsg", "Product Not Added! Something wrong on Server side");
		} else {
			session.setAttribute("successMsg", "Product Added successfully.");
		}
		
		return "redirect:/admin/products";
	}
	
	@GetMapping("/editProduct/{id}")
	public String editProduct(@PathVariable Integer id, Model model) {
		model.addAttribute("product", productService.getProduct(id));
		model.addAttribute("categories", categoryService.getAllCategory());
		return "admin/edit_product";
	}
	
	@PostMapping("/updateProduct")
	public String updateProduct(@ModelAttribute Product product, 
	        @RequestParam MultipartFile file, HttpSession session) {
	    
	    Product oldProduct = productService.getProduct(product.getId());
	    if (ObjectUtils.isEmpty(oldProduct)) {
	        session.setAttribute("errorMsg", "Product not found!");
	        return "redirect:/admin/category";
	    }
	    
	    if(product.getDiscount() < 0 || product.getDiscount() > 100) {
	        session.setAttribute("errorMsg", "Invalid discount value. Must be between 0 and 100.");
	        return "redirect:/admin/editProduct/" + product.getId();
	    }
	    
	    oldProduct.setProductName(product.getProductName());
	    oldProduct.setDescription(product.getDescription());
	    oldProduct.setCategory(product.getCategory());
	    oldProduct.setIsActive(product.getIsActive());
	    oldProduct.setPrice(product.getPrice());
	    oldProduct.setStock(product.getStock());
	    oldProduct.setDiscount(product.getDiscount());
	    oldProduct.setDiscountPrice(product.getPrice() - (product.getPrice() * ((double)product.getDiscount() / 100)));
	    //(70 - (70 * (15/100))) -- (70- (70 * 0.15)) -- (70 - 10.5) -- 59.5

	    Product updateProduct = null;
	    try {
	        if (!file.isEmpty()) {
	            oldProduct.setImage(file.getOriginalFilename());
	            updateProduct = productService.saveProduct(oldProduct, file);
	        } else {
	            updateProduct = productService.updateProduct(oldProduct);
	        }
	    } catch (IOException e) {
	        session.setAttribute("errorMsg", "Product not updated! Internal server error: " + e.getMessage());
	    } catch (Exception e) {
	        session.setAttribute("errorMsg", "Unexpected error occurred: " + e.getMessage());
	    }
	    
	    if (ObjectUtils.isEmpty(updateProduct)) {
	        session.setAttribute("errorMsg", "Product not updated! Something went wrong on the server.");
	    } else {
	        session.setAttribute("successMsg", "Product updated successfully.");
	    }
	    
	    return "redirect:/admin/editProduct/" + product.getId();
	}
	
	@GetMapping("/deleteProduct/{id}")
	public String deleteProduct(@PathVariable Integer id, HttpSession session) {
		
		Boolean deleteProduct = productService.deleteProduct(id);
		if(!ObjectUtils.isEmpty(deleteProduct)) {
			session.setAttribute("successMsg", "Product deleted Successfully.");
		} else {
			session.setAttribute("errorMsg", "Product Not deleted. Something wrong on server side.");
		}
		
		return "redirect:/admin/products";
	}
	
	/*
	 * User section
	 */
	
	@GetMapping("/users")
	public String getAllUsers(Model model,
			@RequestParam(defaultValue = "0") Integer pageNo,
			@RequestParam(defaultValue = "6") Integer pageSize) {
		
//		model.addAttribute("users", userService.getAllUsers("ROLE_USER"));
		
		Page<UserDtls> page = userService.getAllUsers("ROLE_USER", pageNo, pageSize);
		
		List<UserDtls> users = page.getContent();
		model.addAttribute("users", users);
		model.addAttribute("usersSize", users.size());
		model.addAttribute("pageNo", page.getNumber());
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("totalElements", page.getTotalElements());
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("isFirst", page.isFirst());
		model.addAttribute("isLast", page.isLast());
		
		return "admin/users";
	}
	
	@GetMapping("/updateStatus")
	public String updateUserAccountStatus(@RequestParam Integer id, @RequestParam Boolean status, HttpSession session) {
		Boolean updateUserStatus = userService.updateAccountStatus(id, status);
		if(updateUserStatus) {
			session.setAttribute("successMsg", "User Status Updated Successfully.");
		} else {
			session.setAttribute("errorMsg", "User Status Not Updated. Something wrong on server side.");
		}
		return "redirect:/admin/users";
	}
	
	/*
	 * Order section
	 */
	
	@GetMapping("/orders")
	public String allOrders(Model model,
			@RequestParam(defaultValue = "0") Integer pageNo,
			@RequestParam(defaultValue = "6") Integer pageSize) {
		

		List<String> timeFilters = orderService.getTimeFilters();
		List<String> orderStatuses = orderService.getOrderStatuses();
		
//		List<ProductOrder> productOrders = orderService.getAllOrders();
		Page<ProductOrder> page = orderService.getAllOrdersPagination(pageNo, pageSize);
		List<ProductOrder> productOrders = page.getContent();
		
		
		
		
		if((!ObjectUtils.isEmpty(productOrders)) && (!ObjectUtils.isEmpty(timeFilters))) {
			model.addAttribute("timeFilters", timeFilters);
			model.addAttribute("orderStatuses",  orderStatuses);
			model.addAttribute("orders", productOrders);
			model.addAttribute("ordersSize", productOrders.size());
			model.addAttribute("pageNo", page.getNumber());
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("totalElements", page.getTotalElements());
			model.addAttribute("totalPages", page.getTotalPages());
			model.addAttribute("isFirst", page.isFirst());
			model.addAttribute("isLast", page.isLast());
			
			model.addAttribute("selectedStatus", null);
		    model.addAttribute("selectedTime", null);
		}
		
		return "/admin/orders";
	}
	
	@GetMapping("/filterMyOrders")
	public String filterOrders(
			@RequestParam(value = "status", required = false) List<String> selectedStatus,
            @RequestParam(value = "time", required = false) List<String> selectedTime,
            @RequestParam(defaultValue = "0") Integer pageNo,
			@RequestParam(defaultValue = "3") Integer pageSize,
            Model model) {
		
	    if (selectedStatus == null) {
	        selectedStatus = new ArrayList<>();
	    }
	    if (selectedTime == null) {
	        selectedTime = new ArrayList<>();
	    }
		

//	    List<ProductOrder> orders = orderService.filterOrdersByYearAndStatus(selectedStatus, selectedTime);
	    Page<ProductOrder> page = orderService.filterOrdersByYearAndStatusPagination(selectedStatus, selectedTime, pageNo, pageSize);
	    List<ProductOrder> orders = page.getContent();
	    

	    List<String> timeFilters = orderService.getTimeFilters();
	    List<String> orderStatuses = orderService.getOrderStatuses();
	    
//	    Collections.reverse(orders);

	    model.addAttribute("orders", orders);
	    
	    
	    if((!ObjectUtils.isEmpty(orders)) && (!ObjectUtils.isEmpty(timeFilters))) {
			model.addAttribute("timeFilters", timeFilters);
			model.addAttribute("orderStatuses",  orderStatuses);
			model.addAttribute("orders", orders);
			model.addAttribute("ordersSize", orders.size());
			model.addAttribute("pageNo", page.getNumber());
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("totalElements", page.getTotalElements());
			model.addAttribute("totalPages", page.getTotalPages());
			model.addAttribute("isFirst", page.isFirst());
			model.addAttribute("isLast", page.isLast());
			model.addAttribute("selectedStatus", selectedStatus);
		    model.addAttribute("selectedTime", selectedTime);
		}

	    return "/admin/orders";
	}
	
	@GetMapping("/searchMyOrders")
    public String searchOrders(@RequestParam Long productOrderId, Model model) {
		List<ProductOrder> orders = orderService.searchOrders(productOrderId);
		
		List<String> timeFilters = orderService.getTimeFilters();
	    List<String> orderStatuses = orderService.getOrderStatuses();
	    
	    Collections.reverse(orders);
	    
		model.addAttribute("orders", orders);
	    model.addAttribute("orderStatuses", orderStatuses);
	    model.addAttribute("timeFilters", timeFilters);
	    
		return "/admin/orders";
	}
	
	@PostMapping("/update-order-status")
    public String updateOrderStatus(@RequestParam Integer id, @RequestParam Integer statusId, HttpSession session) {
		
		try {
			ProductOrder order =orderService.updateOrderStatus(id, statusId);
			Boolean sendMail = commanUtil.sendMailForProductOrder(order, order.getStatus().getStatus());
			if(sendMail) {
				session.setAttribute("successMsg", "Order status updated successfully.");
			} else {
				session.setAttribute("errorMsg", "Order status update Failed.");
			}            
        } catch (Exception e) {
        	session.setAttribute("errorMsg", "Something wrong on server side");
        }
		 return "redirect:/admin/orders";
	}
	
	/*
	 * Admin Section
	 */
	
	@GetMapping("/add-admin")
	public String loadAdminAdd() {
		return "/admin/add_admin";
	}
	
	@PostMapping("/saveAdmin")
	public String saveAdmin(@ModelAttribute UserDtls user, @RequestParam("img") MultipartFile file, HttpSession session) {
		String imageName = file.isEmpty() ? "default.png" : file.getOriginalFilename();
		user.setProfileImage(imageName);
		
		if(userService.isExistUser(user.getEmail())) {
			session.setAttribute("errorMsg", "Admin Already Exist");
		} else {
			UserDtls saveUser = null;		
			try {
				saveUser = userService.saveAdmin(user, file);
			} catch (Exception e) {
				session.setAttribute("errorMsg", "Admin Not Register! Internal Server Error\n"+e.getMessage());
			}
			
			if(ObjectUtils.isEmpty(saveUser)) {
				session.setAttribute("errorMsg", "Admin Not Register! Internal Server Error");
			} else {
				session.setAttribute("successMsg", "Admin Register Successfully");
			}
		}
		
		return "redirect:/admin/add-admin";
	}
	
	@GetMapping("/profile")
	public String profile() {
		return "/admin/profile";
	}
	
	@PostMapping("/uploadProfileImage")
    public String uploadProfileImage(@RequestParam MultipartFile profileImage, HttpSession session, Principal principal) {
		
		if (profileImage.isEmpty()) {
        	session.setAttribute("errorMsg", "No file selected for upload.");
            return "redirect:/user/profile";
        }
        
        String image = (!ObjectUtils.isEmpty(profileImage)) ? profileImage.getOriginalFilename() : "default.jpg";
        UserDtls user = userService.getLoggedInUserDetails(principal);
        user.setProfileImage(image);
        
        UserDtls updateProfileImage = null;

        try {
        	updateProfileImage = userService.uploadProfileImage(user, profileImage);
        } catch (IOException e) {
            e.printStackTrace();
            session.setAttribute("errorMsg", "Failed to upload the profile image.");
        } catch (Exception e) {
			session.setAttribute("errorMsg", "Unexpected error occurred: " + e.getMessage());
		}
		
		if(ObjectUtils.isEmpty(updateProfileImage)) {
			session.setAttribute("errorMsg", "Profile Image Not updated! Something wrong on Server side");
		} else {
			session.setAttribute("successMsg", "Profile image updated successfully.");
		}
        return "redirect:/admin/profile";
    }
	
	@PostMapping("/updateProfile")
	public String updateProfile(@ModelAttribute UserDtls userDtls, HttpSession session, Principal principal) {
		
		if(userDtls == null) {
			session.setAttribute("errorMsg", "Update Information is missing..");
			return "redirect:/user/profile";
		}
		
		UserDtls user = userService.getLoggedInUserDetails(principal);
		
		UserDtls updateProfileUser = userService.updateProfile(user, userDtls);
		
		if(updateProfileUser != null) {
			session.setAttribute("successMsg", "Profile updated successfully.");
		} else {
			session.setAttribute("errorMsg", "Profile Not updated! Something wrong on Server side");
		}
		
		return "redirect:/admin/profile";
	}
	
	@PostMapping("/changePassword")
	public String changePassword(@RequestParam String password, @RequestParam String newPassword, Principal principal, RedirectAttributes redirectAttributes) {
		
		UserDtls user = userService.getLoggedInUserDetails(principal);
		Boolean matches = passwordEncoder.matches(password,user.getPassword());
		if(matches) {
			String encodePassword = passwordEncoder.encode(newPassword);
			user.setPassword(encodePassword);
			UserDtls updatePasswordUser = userService.updateUser(user);
			if (ObjectUtils.isEmpty(updatePasswordUser)) {
	            redirectAttributes.addFlashAttribute("errorMsg", "Password Not updated! Something went wrong on the server side.");
	        } else {
	            redirectAttributes.addFlashAttribute("successMsg", "Password updated successfully.");
	        }
	    } else {
	        redirectAttributes.addFlashAttribute("errorMsg", "Current Password Incorrect");
	    }
	    
	    return "redirect:/admin/profile#update-password";
	}
}
