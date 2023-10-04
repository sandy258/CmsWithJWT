package com.user.controller;

import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.user.exception.AdNotFoundException;
import com.user.models.Ad;
import com.user.models.Category;
import com.user.service.UserServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/cms")
public class UserController {
	
	@Autowired
	private UserServiceImpl userServiceImpl;
	

	@PostMapping("/category")
	@PreAuthorize("hasRole('ADMIN')")
	ResponseEntity<Category> createCategory(@Valid @RequestBody Category category){
		Category category2 = userServiceImpl.createCategory(category);
		return new ResponseEntity<Category>(category2,HttpStatus.CREATED);
	}
	
	@PostMapping("/Ad")
	ResponseEntity<Ad> createAd(@Valid @RequestBody Ad ad){
		return new ResponseEntity<Ad>(userServiceImpl.createAd(ad),HttpStatus.CREATED);
	}
	

	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	ResponseEntity<List<Ad>> getAllAd(){
		return new ResponseEntity<>(userServiceImpl.getAllAds(),HttpStatus.OK);
	}
	
	@GetMapping("/categoryId/{categoryId}")
	ResponseEntity<List<Ad>> getAllAdByCategoryId(@PathVariable int categoryId) {
		return new ResponseEntity<>(userServiceImpl.getAllAdByCategoryId(categoryId),HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	ResponseEntity<Ad> deleteAd(@PathVariable int id) throws AdNotFoundException {
		return new ResponseEntity<>(userServiceImpl.deleteAd(id),HttpStatus.OK);
	}
	
	@GetMapping("/currentUser")
	public String getLoggedInUserDetails(Principal principal) {
		return principal.getName();
	}
}
