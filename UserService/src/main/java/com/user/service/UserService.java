package com.user.service;

import java.util.List;

import com.user.exception.AdNotFoundException;
import com.user.exception.CategoryNotFoundException;
import com.user.exception.UserNotFoundException;
import com.user.models.Ad;
import com.user.models.Category;
import com.user.models.User;


public interface UserService {
	
	
	public User createUser(User user);
	
	public List<Ad> getAllAds();
	public Category createCategory(Category category);
	
	
	public Ad deleteAd(int id) throws AdNotFoundException;
	public Ad createAd(Ad ad);
	public List<Ad> getAllAdByCategoryId(int categoryId);
	public Category findCategoryById(int id) throws CategoryNotFoundException;
	public Ad findAdById(int id) throws AdNotFoundException;
	public User findUserById(int id) throws UserNotFoundException;
}
