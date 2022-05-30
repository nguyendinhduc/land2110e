package com.t3h.land2110e.service;

import com.t3h.land2110e.entity.UserProfileEntity;
import com.t3h.land2110e.model.CreatingUserRequest;
import com.t3h.land2110e.model.request.LoginRequest;
import com.t3h.land2110e.model.request.RegisterRequest;
import com.t3h.land2110e.model.response.LoginResponse;
import com.t3h.land2110e.model.response.ResponseException;
import com.t3h.land2110e.repository.UserProfileRepository;
import com.t3h.land2110e.security.AuthorizationFilter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {
    @Autowired
    private UserProfileRepository userProfileRepository;


    private String getJWT(UserProfileEntity user){
        Claims claims = Jwts.claims()
                .setSubject(user.getUsername());
        claims.setId(user.getId()+"");
        claims.put("username", user.getUsername());
        claims.put("avatar", user.getAvatar());

        return Jwts.builder().setClaims(claims)
                .signWith( SignatureAlgorithm.HS512, "123a@")
                .setExpiration(new Date(new Date().getTime() + 24*60*60*1000L))
                .compact();
    }

    public Object login(LoginRequest request) throws ResponseException {
//        1: kiem tra user ton tai trong DB khong
//        2. kiem mat khau
        UserProfileEntity user = this.userProfileRepository.findOneByUsername(
                request.getUsername()
        );
        if ( user == null ){
            throw new ResponseException("username not exist");
        }

        if ( new BCryptPasswordEncoder().matches(request.getPassword(),  user.getPassword())){
            //phải trả về token
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setToken(getJWT(user));
            loginResponse.setUser(user);
            return loginResponse;
        }
        throw new ResponseException("Password invalid");

    }

    public Object findAll() {
        return userProfileRepository.findAll();
    }

    public Object createUser(CreatingUserRequest user) {
        UserProfileEntity userProfileEntity = new UserProfileEntity();
        userProfileEntity.setUsername(user.getUsername());
        userProfileEntity.setAvatar(user.getAvatar());
        userProfileEntity.setPassword(user.getPassword());
        userProfileEntity.setEmail(user.getEmail());
        userProfileEntity = this.userProfileRepository.save(userProfileEntity);
        return userProfileEntity;
    }

    public Object register(RegisterRequest request) throws ResponseException{
        UserProfileEntity user = this.userProfileRepository.findOneByUsername(request.getUsername());
        if ( user != null ){
            throw new ResponseException("this Username "+ request.getUsername()+" existed");
        }
        UserProfileEntity entity = new UserProfileEntity();
        entity.setFirstName(request.getFirstName());
        entity.setLastName(request.getLastName());
        entity.setUsername(request.getUsername());
        entity.setEmail(request.getEmail());
        entity.setAvatar(request.getAvatar());
        //encode password
        entity.setPassword(new BCryptPasswordEncoder().encode(request.getPassword()));

        entity = this.userProfileRepository.save(entity);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(getJWT(entity));
        loginResponse.setUser(entity);
        return loginResponse;
    }

    public Object getMyProfile() {
        int userId = AuthorizationFilter.getCurrentUserId();
        UserProfileEntity user = this.userProfileRepository.findOneById(userId);
        if (user == null ){
            throw new ResponseException("User not exist");
        }
        return user;
    }
}
