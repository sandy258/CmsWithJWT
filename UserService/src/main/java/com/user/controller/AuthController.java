package com.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.models.JWTRequest;
import com.user.models.JwtResponse;
import com.user.models.User;
import com.user.security.JwtHelper;
import com.user.service.UserServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private UserDetailsService  detailsService;
	
	@Autowired
	private JwtHelper helper;
	
	@Autowired
	private AuthenticationManager authenticationManager;	
	
	@Autowired
	private UserServiceImpl userServiceImpl;
	
	@PostMapping("/create")
	ResponseEntity<User> createUser(@Valid @RequestBody User user){
		return new ResponseEntity<User>(userServiceImpl.createUser(user),HttpStatus.CREATED);
	}
	
	
	@PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JWTRequest request) {
        this.doAuthenticate(request.getEmail(), request.getPassword());
        UserDetails userDetails = detailsService.loadUserByUsername(request.getEmail());
        String token = this.helper.generateToken(userDetails);
        JwtResponse response = JwtResponse.builder()
                .jwtToken(token)
                .username(userDetails.getUsername()).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
	
	 private void doAuthenticate(String email, String password) {
		 UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
	     try{
	    	 	authenticationManager.authenticate(authentication);
	        } 
	     catch(BadCredentialsException e) {
	            throw new BadCredentialsException(" Invalid Username or Password  !!");
	        }
	    }
}
