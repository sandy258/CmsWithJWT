package com.user.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.models.User;

public interface UserRepo extends JpaRepository<User, Integer>{
	Optional<User> findByEmail(String email);
}
