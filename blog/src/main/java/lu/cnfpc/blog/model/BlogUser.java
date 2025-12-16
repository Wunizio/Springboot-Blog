package lu.cnfpc.blog.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    @NotBlank
    @Size(min=3, max=25)
    private String name;

    @NotBlank
    @Size(min=3)
    private String password;

    @PastOrPresent
    private LocalDateTime creationDate;

    @PastOrPresent
    private LocalDateTime lastLogin;

    private String profilePicture;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "blogUser")
    private List<Post> posts;


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
    
}
