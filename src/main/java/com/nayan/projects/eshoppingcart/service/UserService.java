package com.nayan.projects.eshoppingcart.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.nayan.projects.eshoppingcart.model.UserDtls;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {

	public UserDtls saveUser(UserDtls user, MultipartFile file) throws IOException;

	public boolean isExistUser(String email);
	
	public UserDtls getUserByEmail(String email);
	
	public List<UserDtls> getAllUsers(String role);

	public Boolean updateAccountStatus(Integer userId, Boolean status);
	
	public void increaseFailedAttempt(UserDtls user);
	
	public void userAccountLock(UserDtls user);
	
	public Boolean unlockAccountTimeExpired(UserDtls user);
	
	public void resetAttempts(Integer userId);

	public Boolean sendMail(String email, String resetToken, HttpServletRequest request) throws UnsupportedEncodingException, MessagingException ;

	public void updateUserResetToken(String email, String resetToken);
	
	public UserDtls getUserByToken(String token);
	
	public UserDtls updateUser(UserDtls user);//updated by admin

	public UserDtls getLoggedInUserDetails(Principal principal);

	public Boolean registerNotification(Integer pid, Integer uid);
	
	public UserDtls uploadProfileImage(UserDtls user, MultipartFile file) throws IOException;
	
	public UserDtls updateProfile(UserDtls logUser, UserDtls updatedDtls);

	public Page<UserDtls> getAllUsers(String string, Integer pageNo, Integer pageSize);

	public UserDtls saveAdmin(UserDtls user, MultipartFile file) throws IOException;
}
