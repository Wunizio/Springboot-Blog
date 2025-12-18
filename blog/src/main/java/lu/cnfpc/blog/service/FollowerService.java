package lu.cnfpc.blog.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lu.cnfpc.blog.exception.FollowerNotFoundException;
import lu.cnfpc.blog.model.BlogUser;
import lu.cnfpc.blog.model.Follower;
import lu.cnfpc.blog.repository.FollowerRepository;

@Service
@Transactional
public class FollowerService {
    @Autowired
    private final FollowerRepository followerRepository;

    public FollowerService(FollowerRepository followerRepository){
        this.followerRepository = followerRepository;
    }

    public void submitFollower(Follower follower){
        followerRepository.save(follower);
    }

    public void removeFollower(Follower follower){
        followerRepository.delete(follower);
    }

    public boolean isUserAFollowingUserB(BlogUser userA, BlogUser userB){
        Follower followTable = followerRepository.findByFollowingAndFollower(userB, userA).orElseThrow(() -> new FollowerNotFoundException("This should not show up. isUserAFollowingUserB"));
        return followTable != null ? true : false;
    }

    public Follower findFollower(BlogUser userA, BlogUser userB){
        return followerRepository.findByFollowingAndFollower(userB, userA).orElseThrow(() -> new FollowerNotFoundException(userA.getName() + " is not a follower of " +userB.getName()));
    }

    public List<BlogUser> findUsersFollowedByThisUser(BlogUser user){
        List<Follower> followers = followerRepository.findAllByFollowing(user);
        List<BlogUser> users = new ArrayList<>();
        for(Follower follower : followers){
            users.add(follower.getFollower());
        }
        return users;
    }

    public List<BlogUser> findUsersFollowingThisUser(BlogUser user){
        List<Follower> followers = followerRepository.findAllByFollower(user);
        List<BlogUser> users = new ArrayList<>();
        for(Follower follower : followers){
            users.add(follower.getFollowing());
        }
        return users;
    }
}
