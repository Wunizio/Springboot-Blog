package lu.cnfpc.blog.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import lu.cnfpc.blog.model.BlogUser;
import lu.cnfpc.blog.model.Follower;
import lu.cnfpc.blog.model.FollowerKey;

@Repository
public interface FollowerRepository extends JpaRepository<Follower, FollowerKey> {
    Optional<Follower> findByFollowingAndFollower(BlogUser following, BlogUser follower);
    List<Follower> findAllByFollowing(BlogUser following);
    List<Follower> findAllByFollower(BlogUser follower);
}
