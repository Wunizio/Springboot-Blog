package lu.cnfpc.blog.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

@Entity
public class BlogUser {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long blogUserId;

    @Column(unique=true)
    @NotBlank(message = "Must have a name.")
    @Size(min=3, max=25, message = "Name must be between 3 and 25 characters.")
    private String name;

    @NotBlank(message = "Must have a password.")
    @Size(min=3, message = "Password but be at least 3 characters.")
    private String password;

    @PastOrPresent
    private LocalDateTime creationDate;

    @PastOrPresent
    private LocalDateTime lastLogin;

    private String profilePicture;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "blogUser")
    private List<Post> posts;

    //List of Follower Objects where this user is getting followed
    @OneToMany(mappedBy="following")
    private List<Follower> follower;

    //List of Blogusers this user is following
    @OneToMany(mappedBy="follower")
    private List<Follower> following;


    public BlogUser(){
        setCreationDate(LocalDateTime.now());
    }
    public Long getBlogUserId() {
        return blogUserId;
    }

    public void setBlogUserId(Long userId) {
        this.blogUserId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public List<Post> getPosts() {
        return posts;
    }
    
    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<Follower> getFollower() {
        return follower;
    }

    public void setFollower(List<Follower> follower) {
        this.follower = follower;
    }

    public List<Follower> getFollowing() {
        return following;
    }
    
    public void setFollowing(List<Follower> following) {
        this.following = following;
    }
    
}
