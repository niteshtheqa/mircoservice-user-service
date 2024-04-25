package com.devops.user.service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devops.user.service.entities.User;

public interface UserRepository extends JpaRepository<User, String>{
    
}
