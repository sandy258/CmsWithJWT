package com.user.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.models.Category;

public interface CategoryRepo extends JpaRepository<Category, Integer>{

}
