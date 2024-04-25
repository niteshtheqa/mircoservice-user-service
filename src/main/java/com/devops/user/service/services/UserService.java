package com.devops.user.service.services;

import java.util.List;

import com.devops.user.service.entities.User;

public interface UserService {
    // User Operations

    // Save Users
    User saveUser(User user);

    // Get All user information
    List<User> getAllUser();

    // get Single user of given UserId

    User getUser(String userId);

    // TODO
    // delete
    // update
}
