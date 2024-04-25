package com.devops.user.service.services.Impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devops.user.service.entities.User;
import com.devops.user.service.exceptions.ResourceNotFoundException;
import com.devops.user.service.repositories.UserRepository;
import com.devops.user.service.services.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepositry;

    @Override
    public User saveUser(User user) {
        String randomUserID = UUID.randomUUID().toString();
        user.setUserId(randomUserID);
        return userRepositry.save(user);

    }

    @Override
    public List<User> getAllUser() {
        return userRepositry.findAll();

    }

    @Override
    public User getUser(String userId) {
        return userRepositry.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usre with given Id is not found" + userId));

    }

}
