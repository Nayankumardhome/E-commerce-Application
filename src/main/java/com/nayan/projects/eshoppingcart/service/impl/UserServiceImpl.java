package com.nayan.projects.eshoppingcart.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.nayan.projects.eshoppingcart.model.Product;
import com.nayan.projects.eshoppingcart.model.ProductNotificationRequest;
import com.nayan.projects.eshoppingcart.model.UserDtls;
import com.nayan.projects.eshoppingcart.repository.ProductNotificationRequestRepository;
import com.nayan.projects.eshoppingcart.repository.UserRepository;
import com.nayan.projects.eshoppingcart.service.UserService;
import com.nayan.projects.eshoppingcart.util.AppConstant;
import com.nayan.projects.eshoppingcart.util.CommanUtil;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private CommanUtil commanUtil;

	@Autowired
	private ProductNotificationRequestRepository notificationRequestRepository;

	@Override
	public UserDtls saveUser(UserDtls user, MultipartFile file) throws IOException{

		File saveFile = new ClassPathResource("/static/img").getFile();

		if(!saveFile.exists()) {
			saveFile.mkdirs();
		}

		Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "profile_img" 
				+ File.separator + file.getOriginalFilename());

		Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

		user.setRole("ROLE_USER");
		user.setIsEnable(true);
		String encodePassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodePassword);
		user.setAccountNonLocked(true);
		user.setFailedAttempts(0);
		user.setLockTime(null);
		return userRepository.save(user);
	}

	@Override
	public boolean isExistUser(String email) {
		return (!ObjectUtils.isEmpty(userRepository.findByEmail(email)));
	}

	@Override
	public UserDtls getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public List<UserDtls> getAllUsers(String role) {
		return userRepository.findByRole(role);
	}

	@Override
	public Boolean updateAccountStatus(Integer userId, Boolean status) {

		Optional<UserDtls> findUser = userRepository.findById(userId);
		if(findUser.isPresent()) {
			UserDtls user = findUser.get();
			user.setIsEnable(status);
			userRepository.save(user);
			return true;
		}

		return false;
	}

	@Override
	public void increaseFailedAttempt(UserDtls user) {
		user.setFailedAttempts(user.getFailedAttempts()+1);
		userRepository.save(user);
	}

	@Override
	public void userAccountLock(UserDtls user) {
		user.setAccountNonLocked(false);
		user.setLockTime(new Date());
		userRepository.save(user);
	}

	@Override
	public Boolean unlockAccountTimeExpired(UserDtls user) {
		long lockTime = user.getLockTime().getTime();
		long unLockTime = lockTime + AppConstant.UNLOCK_DURATION_TIME;

		long currentTime = System.currentTimeMillis();

		if (unLockTime < currentTime) {
			user.setAccountNonLocked(true);
			user.setFailedAttempts(0);
			user.setLockTime(null);
			userRepository.save(user);
			return true;
		}

		return false;
	}

	@Override
	public void resetAttempts(Integer userId) {

	}

	@Override
	public Boolean sendMail(String email, String resetToken, HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
		//Generate URL: http://localhost:8080/reset-password?token=fdivasubiunimvnvao
		String url = CommanUtil.generateUrl(request) + "/reset-password?token=" + resetToken;

		return commanUtil.sendMail(email,url);
	}

	@Override
	public void updateUserResetToken(String email, String resetToken) {
		UserDtls user = userRepository.findByEmail(email);
		if(!ObjectUtils.isEmpty(user)) {
			user.setResetToken(resetToken);
			userRepository.save(user);
		}
	}

	@Override
	public UserDtls getUserByToken(String token) {
		return userRepository.findByResetToken(token);
	}

	@Override
	public UserDtls updateUser(UserDtls user) {
		return userRepository.save(user);
	}

	@Override
	public UserDtls getLoggedInUserDetails(Principal principal) {
		return userRepository.findByEmail(principal.getName());
	}

	@Override
	public Boolean registerNotification(Integer pid, Integer uid) {
		try {
			ProductNotificationRequest notificationRequest = new ProductNotificationRequest();
			notificationRequest.setProductId(pid);
			notificationRequest.setUserId(uid);
			notificationRequestRepository.save(notificationRequest);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public UserDtls uploadProfileImage(UserDtls user, MultipartFile file) throws IOException {

		File saveFile = new ClassPathResource("/static/img").getFile();

		if(!saveFile.exists()) {
			saveFile.mkdirs();
		}

		Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "profile_img" 
				+ File.separator + file.getOriginalFilename());

		System.out.println(path);
		try {
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}

		return userRepository.save(user);
	}

	@Override
	public UserDtls updateProfile(UserDtls logUser, UserDtls updatedDtls) {

		if((logUser != null) && (updatedDtls != null)){
			logUser.setName(updatedDtls.getName());
			logUser.setMobileNumber(updatedDtls.getMobileNumber());
			logUser.setAddress(updatedDtls.getAddress());
			logUser.setCity(updatedDtls.getCity());
			logUser.setState(updatedDtls.getState());
			logUser.setPincode(updatedDtls.getPincode());
			return userRepository.save(logUser);
		}
		return null;
	}

	@Override
	public Page<UserDtls> getAllUsers(String string, Integer pageNo, Integer pageSize) {
		
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		return userRepository.findByRole(string,pageable);
	}

	@Override
	public UserDtls saveAdmin(UserDtls user, MultipartFile file) throws IOException {

		File saveFile = new ClassPathResource("/static/img").getFile();

		if(!saveFile.exists()) {
			saveFile.mkdirs();
		}

		Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "profile_img" 
				+ File.separator + file.getOriginalFilename());

		Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

		user.setRole("ROLE_ADMIN");
		user.setIsEnable(true);
		String encodePassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodePassword);
		user.setAccountNonLocked(true);
		user.setFailedAttempts(0);
		user.setLockTime(null);
		return userRepository.save(user);
	}

}
