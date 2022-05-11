package com.t3h.land2110e.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MakingFriendRequest {
    @JsonProperty(required = true)
    private int friendId;

    public int getFriendId() {
        return friendId;
    }

    public void setFriendId(int friendId) {
        this.friendId = friendId;
    }
}
