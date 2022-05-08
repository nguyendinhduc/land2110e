package com.t3h.land2110e.controller;

import com.t3h.land2110e.entity.UserProfileEntity;
import com.t3h.land2110e.model.CreatingUserRequest;
import com.t3h.land2110e.model.request.LoginRequest;
import com.t3h.land2110e.model.response.ResponseException;
import com.t3h.land2110e.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;

//@Service
//@Component
//@Repository
@RestController
public class UsersController {
    @Autowired
    private UserService userService;

    //    http: GET, POST, PUT, DELETE, PATCH, OPTIONS
//    GET
    @GetMapping("/users")
    public Object getAllUser() {
        return userService.findAll();
    }

    @PostMapping("/users")
    public Object createUser(@RequestBody CreatingUserRequest user) throws Exception {

        return this.userService.createUser(user);
    }

    @PostMapping("/users/login")
    public Object login(
            @RequestBody LoginRequest request
    ) throws ResponseException {
        return this.userService.login(request);
    }
    //1. ket ban
//    => input:
    //2. dong y/huy het ban


}
