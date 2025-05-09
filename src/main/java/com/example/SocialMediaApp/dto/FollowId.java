package com.example.SocialMediaApp.dto;

import java.io.Serializable;
import java.util.Objects;

public class FollowId implements Serializable {
    private Long followerId;
    private Long followingId;

    public FollowId(){}

    public FollowId(Long followerId, Long followingId) {
        this.followerId = followerId;
        this.followingId = followingId;
    }
    @Override
    public boolean equals(Object o){
        if(this ==o) return true;
        if(!(o instanceof FollowId)) return false;
        FollowId that = (FollowId) o;
        return Objects.equals(followerId,that.followerId) && Objects.equals(followingId,that.followingId);
    }
    @Override
    public int hashCode(){
        return Objects.hash(followerId,followingId);
    }
}
