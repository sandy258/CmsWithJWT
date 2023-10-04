package com.user.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.models.Ad;

public interface AdRepo extends JpaRepository<Ad, Integer>{
	List<Ad> findAllByCategoryId(int categoryId);
}
