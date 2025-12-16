package lu.cnfpc.blog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import lu.cnfpc.blog.model.BlogUser;
import lu.cnfpc.blog.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{
    List<Post> findByBlogUser(BlogUser user);
}
