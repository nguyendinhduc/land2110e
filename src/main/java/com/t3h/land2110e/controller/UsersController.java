package com.t3h.land2110e.controller;

import com.t3h.land2110e.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UsersController {
    private List<User> users = new ArrayList<>();
//    http: GET, POST, PUT, DELETE, PATCH, OPTIONS
//    GET
    @GetMapping("/users")
    public Object getAllUser(){
        return users;
    }

    @PostMapping("/users")
    public Object createUser(@RequestBody User user) throws Exception{
        for (User userInDB : users) {
            if (userInDB.getId() == user.getId()){
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "");
            }
        }
        users.add(user);
        return user;
    }



}
