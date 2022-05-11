package com.t3h.land2110e.controller;

import com.t3h.land2110e.model.request.MakingFriendRequest;
import com.t3h.land2110e.model.response.ResponseException;
import com.t3h.land2110e.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FriendController {
    @Autowired
    private FriendService friendService;
    @PostMapping("/api/friend/making")
    public Object makeFriend(@RequestBody MakingFriendRequest request) throws ResponseException{
        return friendService.makeFriend(
                request.getFriendId()
        );
    }

    @PostMapping("/api/friend/accept")
    public Object acceptFriend(
            @RequestParam("friendId") int friendId
    ){
        return friendService.acceptFriend(
                friendId
        );
    }
}
