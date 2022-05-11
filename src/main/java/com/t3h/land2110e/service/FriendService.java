package com.t3h.land2110e.service;

import com.t3h.land2110e.entity.FriendEntity;
import com.t3h.land2110e.entity.UserProfileEntity;
import com.t3h.land2110e.model.response.ResponseException;
import com.t3h.land2110e.repository.FriendRepository;
import com.t3h.land2110e.repository.UserProfileRepository;
import com.t3h.land2110e.security.AuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FriendService {
    @Autowired
    private FriendRepository friendRepository;
    @Autowired
    private UserProfileRepository userProfileRepository;

    public Object makeFriend(int friendId) throws ResponseException {
        int userId = AuthorizationFilter.getCurrentUserId();
        if (userId == friendId ){
            throw new ResponseException("userId not equal friendId");
        }
        //check user tồn tài
        UserProfileEntity user = this.userProfileRepository.findById(userId);
        if ( user == null) {
            throw new ResponseException("User not exist");
        }

        //check friend tồn tài
        UserProfileEntity userFriend = this.userProfileRepository.findById(friendId);
        if ( userFriend == null) {
            throw new ResponseException("Friend not exist");
        }

        FriendEntity friend = this.friendRepository.findOnByUserIdAndFriendId(
                userId, friendId
        );

        //kiem userId co ket ban voi friend khong
        if ( friend != null ){
            if (!friend.getStatus().equals("canceled")){
                throw new ResponseException("You or your friend made friend");
            }else {
                friend.setSenderId(userId);
                friend.setReceiverId(friendId);
                friend.setStatus("pending");
//                update into db
                this.friendRepository.save(friend);
                return friend;
            }
        }

        //kiem friendId co ket ban voi userId khong
        friend = this.friendRepository.findOnByUserIdAndFriendId(
                friendId, userId
        );
        if ( friend != null){
            if (!friend.getStatus().equals("canceled")){
                throw new ResponseException("You or your friend made friend");
            }else {
                friend.setSenderId(userId);
                friend.setReceiverId(friendId);
                friend.setStatus("pending");
//                update into db
                this.friendRepository.save(friend);
                return friend;
            }
        }

        friend = new FriendEntity();
        friend.setSenderId(userId);
        friend.setReceiverId(friendId);
        friend.setStatus("pending");
        friend = this.friendRepository.save(friend);
        return friend;

    }

    public Object acceptFriend(int friendId) {
        int userId = AuthorizationFilter.getCurrentUserId();
        FriendEntity friendEntity = this.friendRepository.findOneBySenderIdAndReceiverId(
                friendId, userId
        );
        if ( friendEntity == null ){
            throw new ResponseException("You can not accepted friend because user "+friendId+" not yet request");
        }
        if ( !"pending".equals(friendEntity.getStatus())){
            throw new ResponseException("Status must be pending");
        }
        friendEntity.setStatus("accepted");
        this.friendRepository.save(friendEntity);
        return friendEntity;

    }
    //lấy ra các bạn bè của thằng id = 4:  id, firstName, lastName, avatar, lastmessage
}
