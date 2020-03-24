package com.template.oauth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.template.oauth.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	User findUserByUsername(String username);
	
	@Query("FROM User WHERE username=?1 OR email=?2")
	List<User> getUsernameOrEmail(String username, String email);
}
