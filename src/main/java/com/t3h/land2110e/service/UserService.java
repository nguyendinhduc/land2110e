package com.t3h.land2110e.service;

import com.t3h.land2110e.entity.UserProfileEntity;
import com.t3h.land2110e.model.CreatingUserRequest;
import com.t3h.land2110e.model.request.LoginRequest;
import com.t3h.land2110e.model.response.ResponseException;
import com.t3h.land2110e.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserProfileRepository userProfileRepository;

    public Object login(LoginRequest request) throws ResponseException {
//        1: kiem tra user ton tai trong DB khong
//        2. kiem mat khau
        UserProfileEntity user = this.userProfileRepository.findOneByUsername(
                request.getUsername()
        );
        if ( user == null ){
            throw new ResponseException("username not exist");
        }
        if ( user.getPassword().equals(request.getPassword())){
            return user;
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
}
