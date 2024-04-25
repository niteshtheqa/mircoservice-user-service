package com.devops.user.service.services.Impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.devops.user.service.entities.Hotel;
import com.devops.user.service.entities.Rating;
import com.devops.user.service.entities.User;
import com.devops.user.service.exceptions.ResourceNotFoundException;
import com.devops.user.service.external.services.HotelService;
import com.devops.user.service.repositories.UserRepository;
import com.devops.user.service.services.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepositry;

    @Autowired
    private RestTemplate restTemplate;


    @Autowired
    private HotelService hotelService;

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

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

        // get user from databas ewith the help of user repository
        User user = userRepositry.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usre with given Id is not found" + userId));

        // fetch rating of the above usre from rating service
        // localhost:8083/ratings/users/708b95a6-b65a-4a07-a987-68a4e40aa124

        Rating[] ratingsOfUser = restTemplate.getForObject(
                "http://RATINGSERVICE/ratings/users/" + user.getUserId(), Rating[].class);

        List<Rating> ratings = Arrays.stream(ratingsOfUser).toList();

        @SuppressWarnings("null")
        List<Rating> ratingList = ratings.stream().map(rating -> {

            // http://localhost:8082/hotels/f408a300-19d9-4924-a8e4-ae2ff1e79fb7
            //ResponseEntity<Hotel> forEntity = restTemplate.getForEntity("http://HOTELSERVICE/hotels/" + rating.getHotelId(), Hotel.class);

            Hotel hotel = hotelService.getHotel(rating.getHotelId());
 
            // logger.info("Response status code: {}", forEntity.getStatusCode());
            // logger.info("Name: {}",forEntity.getBody().getName());
            // logger.info("Hotel Id: {}",forEntity.getBody().getHotelId());
            // logger.info("Location: {}",forEntity.getBody().getLocation());
            
  


            rating.setHotel(hotel);

            return rating;

        }).collect(Collectors.toList());
        user.setRatings(ratingList);

        return user;

    }

}
