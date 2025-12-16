package lu.cnfpc.blog.controller;

import java.io.Console;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import lu.cnfpc.blog.model.BlogUser;
import lu.cnfpc.blog.model.Post;
import lu.cnfpc.blog.service.BlogUserService;
import lu.cnfpc.blog.service.PostService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
public class PostController {
    
    @Autowired
    private final PostService postService;
    @Autowired
    private final BlogUserService blogUserService;

    public PostController(PostService postService, BlogUserService blogUserService){
        this.postService = postService;
        this.blogUserService = blogUserService;
    }

    @GetMapping("/home")
    public String getHome(@RequestParam String name, Model model) {
        System.out.println(name);
        //Not Secure
        model.addAttribute("blogUser", blogUserService.getUserByName(name));
        return "home";
    }

    @GetMapping("/createBlogpost")
    public String getCreateBlog(@RequestParam String name, Model model) {
        BlogUser blogUser = blogUserService.getUserByName(name);
        model.addAttribute("blogUser", blogUser);
        Post post = new Post();
        post.setBlogUser(blogUser);
        model.addAttribute("blogPost", post);
        return "createBlogpost";
    }

    @PostMapping("/handleCreatePost")
    public String createPost(Post post) {
        postService.submitPost(post);
        //Not Secure
        return "redirect:/home?name="+post.getBlogUser().getName();
    }
    
    
    

}
