package com.user.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	private Logger logger = LoggerFactory.getLogger(OncePerRequestFilter.class);

	@Autowired
	private JwtHelper jwtHelper;

	@Autowired
	private UserDetailsService userDetailsService;
	
	

	// this method will be invoked before each request to verify the jwt token and
	// other important things like
	// Get Token from request
	// Validate Token
	// GetUsername from token
	// Load user associated with this token
	// set authentication

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// Finding the header  -> you will find like this Bearer 2352345235sdfrsfgsdfsdf
		String requestHeader = request.getHeader("Authorization");
		// Bearer 2352345235sdfrsfgsdfsdf
		logger.info(" Header :  {}", requestHeader);

		String username = null;
		String token = null;
		if (requestHeader != null && requestHeader.startsWith("Bearer")) {
			token = requestHeader.substring(7); // means 7th index se last tak token hoga 
			try {
				username = this.jwtHelper.getUsernameFromToken(token);
			} 
			
			// if jwt helper class wont give the username then it will throw the IllegalArgumentException
			catch (IllegalArgumentException e) {
				logger.info("Illegal Argument while fetching the username !!");
				e.printStackTrace();
			} 
			
			// if token has been expired and you are asking for the username then it will throw the ExpiredJwtException.
			catch (ExpiredJwtException e) {
				logger.info("Given jwt token is expired !!");
				e.printStackTrace();
			}
			
			// if you changed something in the token then it will throw an exception of MalformedJwtException.
			catch (MalformedJwtException e) {
				logger.info("Some changed has done in token !! Invalid Token");
				e.printStackTrace();
			} 
			
			// if any exception occurs than above these exceptions then i'm handking this 
			catch (Exception e) {
				e.printStackTrace();
			}
		} 
		
		else {
			logger.info("Invalid Header Value !! ");
		}
		
		
		// now you recieved the username and now validate that username from database or from in memory
		// means username mil gya aur wo authenticated nhi hai abhi tak then
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			// fetch user detail from username
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
			
			// validate the taken with the help of the helper class 
			Boolean validateToken = this.jwtHelper.validateToken(token, userDetails);
			
			// if token is valid then
			if (validateToken) {

				// set the authentication
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				//authentication will always be set on the SecurityContextHolder
				// so first fetch the security context with the help of the getContext() 
				// then set the authentication so first create the authentication object above.
				SecurityContextHolder.getContext().setAuthentication(authentication);

			} 
			
			else {
				logger.info("Validation fails !!");
			}

		}
		filterChain.doFilter(request, response);

	}
}
