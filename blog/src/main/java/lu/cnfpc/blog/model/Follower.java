package lu.cnfpc.blog.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

@Entity
public class Follower {
    
    @EmbeddedId
    private FollowerKey followerId;

    //The User getting Followed
    @ManyToOne
    @JoinColumn
    @MapsId("followingKeyId")
    private BlogUser following;

    //The User who clicked the follow button
    @ManyToOne
    @JoinColumn
    @MapsId("followerKeyId")
    private BlogUser follower;

    public Follower(){

    }

    public FollowerKey getFollowerId() {
        return followerId;
    }

    public void setFollowerId(FollowerKey followerId) {
        this.followerId = followerId;
    }

    public BlogUser getFollowing() {
        return following;
    }

    public void setFollowing(BlogUser following) {
        this.following = following;
    }

    public BlogUser getFollower() {
        return follower;
    }

    public void setFollower(BlogUser follower) {
        this.follower = follower;
    }
}