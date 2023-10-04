package com.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.user.exception.AdNotFoundException;
import com.user.exception.CategoryNotFoundException;
import com.user.exception.NotAnAdminException;
import com.user.exception.UserNotFoundException;
import com.user.models.Ad;
import com.user.models.Category;
import com.user.models.User;
import com.user.repo.AdRepo;
import com.user.repo.CategoryRepo;
import com.user.repo.UserRepo;

@ExtendWith(MockitoExtension.class)
public class ServiceTest {
	@InjectMocks
	private UserServiceImpl userServiceImpl;
	
	@InjectMocks
	private CustomUserDetailService customUserDetailService;
	
	@Mock
	private UserRepo userRepo;
	
	@Mock
	private AdRepo adRepo;
	
	@Mock
	private CategoryRepo categoryRepo;
	
	@Mock
	private PasswordEncoder encoder;
	
	
	
	@Test
	void createUserSuccesstest() {
		User user=new User();
		user.setName("sandesh");
		user.setRole("Admin");
		user.setEmail("sandesh@gmail.com");
		user.setPassword("abdc");
		when(encoder.encode(user.getPassword())).thenReturn("abcd");
		when(userRepo.save(user)).thenReturn(user);
		assertEquals(user, userServiceImpl.createUser(user));
	}
	
	
	
	
	
	@Test
	void createCategorySuccessTest(){
		Category category = Category.builder().name("House").build();
		when(categoryRepo.save(category)).thenReturn(category);
		assertEquals(category, userServiceImpl.createCategory(category));
	}
	

	
	@Test
	void createCategoryFailureTest() throws UserNotFoundException, NotAnAdminException {
		var userId=1000;
		when(userRepo.findById(userId)).thenReturn(Optional.empty());
		var exception = assertThrows(UserNotFoundException.class, () -> userServiceImpl.findUserById(userId));
		assertEquals(exception.getLocalizedMessage(), "User Not Found");
	}
	
	@Test
	void findCategorySuccessTest() throws CategoryNotFoundException {
		Category category = Category.builder().name("House").build();
		var id = 1;
		when(categoryRepo.findById(id)).thenReturn(Optional.of(category));
		assertEquals(category, userServiceImpl.findCategoryById(id));
	}

	@Test
	void findCategoryFailureTest() throws CategoryNotFoundException {
		var id = 1;
		when(categoryRepo.findById(id)).thenReturn(Optional.empty());
		var exception = assertThrows(CategoryNotFoundException.class, () -> userServiceImpl.findCategoryById(id));
		assertEquals("Category Not Found", exception.getMessage());
	}
	
	@Test
	void adCreationSuccessTest() {
		Ad ad =Ad.builder()
				.categoryId(1).name("cement")
		.build();
		when(adRepo.save(ad)).thenReturn(ad);
		assertEquals(ad, userServiceImpl.createAd(ad));
	}
	
	@Test
	void findadSuccessTest() throws AdNotFoundException  {
		Ad ad =Ad.builder()
				.categoryId(1).name("cement")
		.build();
		var id=1;
		when(adRepo.findById(id)).thenReturn(Optional.of(ad));
		assertEquals(ad,userServiceImpl.findAdById(id));
	}
	
	@Test
	void findadFailureTest() throws AdNotFoundException {
		var id=1;
		when(adRepo.findById(id)).thenReturn(Optional.empty());
		var exception=assertThrows(AdNotFoundException.class,() ->userServiceImpl.findAdById(id));
		assertEquals("Ad Not Found",exception.getMessage());
	}
	
	@Test
	void getAllAdsTest() throws AdNotFoundException, UserNotFoundException, NotAnAdminException {
		Ad ad =Ad.builder()
				.categoryId(1)
				.name("cement")
				.build();
		List<Ad> ads=new ArrayList<>();
		ads.add(ad);
		when(adRepo.findAll()).thenReturn(ads);
		assertEquals(1,userServiceImpl.getAllAds().size());
	}
	

	
	@Test
	void getAllAdCategoryIdTest() {
		Ad ad =Ad.builder()
				.categoryId(1).name("cement")
		.build();
		List<Ad> ads=new ArrayList<>();
		ads.add(ad);
		when(adRepo.findAllByCategoryId(1)).thenReturn(ads);
		assertEquals(1, userServiceImpl.getAllAdByCategoryId(1).size());
	}
	
	@Test
	void findCategoryById() throws CategoryNotFoundException {
		Category category = Category.builder().name("House").build();
		when(categoryRepo.findById(1)).thenReturn(Optional.of(category));
		assertEquals(category, userServiceImpl.findCategoryById(1));
	}
	
	@Test
	void deleteAdTest() throws AdNotFoundException {
		Ad ad =Ad.builder()
				.categoryId(1)
				.name("cement")
				.build();
		when(adRepo.findById(1)).thenReturn(Optional.of(ad));
		assertEquals(ad, userServiceImpl.deleteAd(1));
	}
	
	@Test
	void findUserByIdSuccessTest() throws UserNotFoundException {
		User user=new User();
		user.setName("sandesh");
		user.setRole("Admin");
		user.setEmail("sandesh@gmail.com");
		user.setPassword("abdc");
		when(userRepo.findById(1)).thenReturn(Optional.of(user));
		assertEquals(user, userServiceImpl.findUserById(1));
	}
	
	@Test
	void findUserByIdFailureTest() throws UserNotFoundException {
		var userId=1;
		when(userRepo.findById(userId)).thenReturn(Optional.empty());
		var exception=assertThrows(UserNotFoundException.class, ()-> userServiceImpl.findUserById(userId));
		assertEquals(exception.getLocalizedMessage(), "User Not Found");
	}
	
	@Test
	void loadUserByUserNameSuccessTest() {
		User user=new User();
		user.setName("sandesh");
		user.setRole("Admin");
		user.setEmail("sandesh@gmail.com");
		user.setPassword("abdc");
		when(userRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
		assertEquals(user,customUserDetailService.loadUserByUsername(user.getEmail()));
	}
	
	@Test
	void loadUserByUserNameFailureTest() {
		var email="NA@gmail.com";
		when(userRepo.findByEmail(email)).thenReturn(Optional.empty());
		var exception=assertThrows(RuntimeException.class, () -> customUserDetailService.loadUserByUsername(email) );
		assertEquals(exception.getLocalizedMessage(),"User Not Found");
	}
}
