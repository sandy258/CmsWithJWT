package com.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.user.exception.AdNotFoundException;
import com.user.exception.CategoryNotFoundException;
import com.user.exception.UserNotFoundException;
import com.user.models.Ad;
import com.user.models.Category;
import com.user.models.User;
import com.user.repo.AdRepo;
import com.user.repo.CategoryRepo;
import com.user.repo.UserRepo;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private AdRepo adRepo;
	
	@Autowired
	private CategoryRepo categoryRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	
	@Override
	public User createUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepo.save(user);
	}
	
	@Override
	public User findUserById(int id) throws UserNotFoundException {
		return userRepo.findById(id).orElseThrow(()-> new UserNotFoundException("User Not Found"));
	}
	
	@Override
	
	public List<Ad> getAllAds() {
		return adRepo.findAll();
	}
	
	@Override
	public Category createCategory(Category category){
		return categoryRepo.save(category);
	}
	
	
	@Override
	public Category findCategoryById(int id) throws CategoryNotFoundException {
		return categoryRepo.findById(id).orElseThrow(()-> new CategoryNotFoundException("Category Not Found"));
	}
	
	
	@Override
	public Ad createAd(Ad ad) {
		return adRepo.save(ad);
	}
	
	@Override
	public Ad findAdById(int id) throws AdNotFoundException {
		
		return adRepo.findById(id).orElseThrow(()-> new AdNotFoundException("Ad Not Found"));
	}
	
	@Override
	public List<Ad> getAllAdByCategoryId(int categoryId) {
		return adRepo.findAllByCategoryId(categoryId);
	}
	
	@Override
	public Ad deleteAd(int id) throws AdNotFoundException {
		Ad ad = findAdById(id);
		adRepo.deleteById(id);
		return ad;
		
	}

	
	



	

	



	



	

	
}
