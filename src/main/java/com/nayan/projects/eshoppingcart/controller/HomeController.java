package com.nayan.projects.eshoppingcart.controller;


import java.security.Principal;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.nayan.projects.eshoppingcart.model.Category;
import com.nayan.projects.eshoppingcart.model.Product;
import com.nayan.projects.eshoppingcart.model.UserDtls;
import com.nayan.projects.eshoppingcart.service.CartService;
import com.nayan.projects.eshoppingcart.service.CategoryService;
import com.nayan.projects.eshoppingcart.service.ProductService;
import com.nayan.projects.eshoppingcart.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@Controller
public class HomeController {
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@ModelAttribute
	public void getUserDetails(Principal principal, Model model) {
		if(!ObjectUtils.isEmpty(principal)) {
			UserDtls user = userService.getUserByEmail(principal.getName());
			model.addAttribute("user", user);
			model.addAttribute("cardItemsCount", cartService.getCartIntemsCount(user.getId()));
		}
		
		model.addAttribute("categories", categoryService.getAllActiveCategory());
	}
	
	@GetMapping("/")
	public String index(Model model) {
		
		List<Category> allActiveCategories = categoryService.getAllActiveCategory().stream()
				.sorted((c1, c2) -> c1.getId().compareTo(c2.getId()))
				.limit(6).toList();
		List<Product> allActiveProducts = productService.getAllActiveProduct("").stream()
				.sorted((p1,p2) -> p2.getId().compareTo(p1.getId()))
				.limit(8).toList();
		
		model.addAttribute("categories", allActiveCategories);
		model.addAttribute("products", allActiveProducts);
		
		return "index";
	}
	
	@GetMapping("/signin")
	public String login() {
		return "login";
	}
	
	@GetMapping("/register")
	public String register() {
		return "register";
	}
	
	@GetMapping("/products")
	public String products(Model model, @RequestParam(defaultValue = "") String category, 
							@RequestParam(defaultValue = "0") Integer pageNo,
							@RequestParam(defaultValue = "16") Integer pageSize) {
		model.addAttribute("categories", categoryService.getAllActiveCategory());
//		model.addAttribute("products", productService.getAllActiveProduct(category));
		model.addAttribute("paramValue", category);
		
		Page<Product> page = productService.getAllActiveProductPagination(pageNo, pageSize, category);
		List<Product> products = page.getContent();		
		model.addAttribute("products", products);
		model.addAttribute("productsSize", products.size());
		model.addAttribute("pageNo", page.getNumber());
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("totalElements", page.getTotalElements());
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("isFirst", page.isFirst());
		model.addAttribute("isLast", page.isLast());
		
		return "product";
	}
	
	@GetMapping("/product/{id}")
	public String viewProduct(@PathVariable int id, Model model) {
		model.addAttribute("product", productService.getProduct(id));
		return "view_product";
	}
	
	@PostMapping("/saveUser")
	public String saveUser(@ModelAttribute UserDtls user, @RequestParam("img") MultipartFile file, HttpSession session) {
		String imageName = file.isEmpty() ? "default.png" : file.getOriginalFilename();
		user.setProfileImage(imageName);
		
		if(userService.isExistUser(user.getEmail())) {
			session.setAttribute("errorMsg", "User Already Exist");
		} else {
			UserDtls saveUser = null;		
			try {
				saveUser = userService.saveUser(user, file);
			} catch (Exception e) {
				session.setAttribute("errorMsg", "Not Register! Internal Server Error\n"+e.getMessage());
			}
			
			if(ObjectUtils.isEmpty(saveUser)) {
				session.setAttribute("errorMsg", "Not Register! Internal Server Error");
			} else {
				session.setAttribute("successMsg", "Register Successfully");
			}
		}
		
		return "redirect:/register";
	}
	
	//Forgot Password code
	@GetMapping("/forgot-password")
	public String showForgatPasswordPage() {
		return "forgot_password";
	}
	
	@PostMapping("/forgot-password")
	public String forgatPassword(@RequestParam String email, HttpSession session, HttpServletRequest request) {
		
		UserDtls user = userService.getUserByEmail(email);
		
		if(ObjectUtils.isEmpty(user)) {
			session.setAttribute("errorMsg", "Invalid email.");
		} else {
			
			String resetToken = UUID.randomUUID().toString();
			System.out.println("Rset Token: " + resetToken);
			userService.updateUserResetToken(email, resetToken);
			
			Boolean sendMail = false;
			try {
				sendMail = userService.sendMail(email, resetToken, request);
			} catch (Exception e) {
				session.setAttribute("errorMsg", "Something exception on server. Mail Not Send");
			}
			
			if(sendMail) {
				session.setAttribute("successMsg", "Please check your email.. Password Reset link send.");
			} else {
				session.setAttribute("errorMsg", "Something wrong on server. Mail Not Send");
			}
		}
		return "redirect:/forgot-password";
	}
	
	@GetMapping("/reset-password")
	public String showResetPasswordPage(@RequestParam String token, HttpSession session, Model model) {
		
		UserDtls user = userService.getUserByToken(token);
		if(ObjectUtils.isEmpty(user)) {
			model.addAttribute("msg", "Your link is Invalid or Expired!!");
			return "message";
		}
		model.addAttribute("token", token);
		return "reset_password";
	}
	
	@PostMapping("/reset-password")
	public String ResetPassword(@RequestParam String token, @RequestParam String password, HttpSession session, Model model) {
		
		UserDtls user = userService.getUserByToken(token);
		if(ObjectUtils.isEmpty(user)) {
			model.addAttribute("msg", "Your link is Invalid or Expired!!");
			return "message";
		} else {
			user.setPassword(passwordEncoder.encode(password));
			user.setResetToken(null);
			UserDtls userDtls = userService.updateUser(user);
			
			if(!ObjectUtils.isEmpty(userDtls)) {
				model.addAttribute("msg", "Password Reset Successfully.");
				return "message";
			} else {
				model.addAttribute("msg", "Something wrong on Server.");
				return "message";
			}
		}
	}
	
	@GetMapping("/searchProduct")
	public String searchProduct(@RequestParam String querie, Model model) {
		
		List<Product> products = productService.searchProduct(querie);
		model.addAttribute("products", products);
		
		return "product";
	}
	
}
