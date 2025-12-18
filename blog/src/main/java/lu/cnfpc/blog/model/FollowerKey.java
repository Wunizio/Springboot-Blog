package lu.cnfpc.blog.model;

import java.io.Serializable;

import jakarta.persistence.Embeddable;

//Composite Key Class
@Embeddable
public class FollowerKey implements Serializable{
    private Long followerKeyId;
    private Long followingKeyId;

    public Long getFollowerId() {
        return followerKeyId;
    }
    public void setFollowerId(Long followerId) {
        this.followerKeyId = followerId;
    }
    public Long getFollowingId() {
        return followingKeyId;
    }
    public void setFollowingId(Long followingId) {
        this.followingKeyId = followingId;
    }
}