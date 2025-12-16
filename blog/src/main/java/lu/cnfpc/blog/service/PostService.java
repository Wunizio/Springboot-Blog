package lu.cnfpc.blog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lu.cnfpc.blog.exception.BlogUserNotFoundException;
import lu.cnfpc.blog.exception.PostNotFoundException;
import lu.cnfpc.blog.model.BlogUser;
import lu.cnfpc.blog.model.Post;
import lu.cnfpc.blog.repository.PostRepository;

@Service
@Transactional
public class PostService {
    @Autowired
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository){
        this.postRepository = postRepository;
    }

    public void submitPost(Post post){
        postRepository.save(post);
    }

    public Post gePost(Long postId){
        return postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post not found with ID " + postId));
    }
    
    public void deletePost(Long postId){
        postRepository.deleteById(postId);
    }

    public List<Post> getAllPostByUser(BlogUser user){
        return postRepository.findByBlogUser(user);
    }
    
}
