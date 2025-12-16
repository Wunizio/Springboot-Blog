package lu.cnfpc.blog.controller;

import java.io.Console;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import lu.cnfpc.blog.model.BlogUser;
import lu.cnfpc.blog.model.Post;
import lu.cnfpc.blog.service.BlogUserService;
import lu.cnfpc.blog.service.PostService;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

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
    public String getHome(HttpSession session, Model model) {
        String userName = (String) session.getAttribute("userName");
        if(userName == null){
            return "redirect:/";
        }

        BlogUser blogUser = blogUserService.getUserByName(userName);
        model.addAttribute("blogUser", blogUser);
        model.addAttribute("posts", postService.getAllPostByUser(blogUser));
        return "home";
    }

    @GetMapping("/createBlogpost")
    public String getCreateBlogPost(HttpSession session, Model model) {
        String userName = (String) session.getAttribute("userName");
        if(userName == null){
            return "redirect:/";
        }

        BlogUser blogUser = blogUserService.getUserByName(userName);
        model.addAttribute("blogUser", blogUser);
        Post post = new Post();
        post.setBlogUser(blogUser);
        model.addAttribute("blogPost", post);
        return "createBlogpost";
    }

    @GetMapping("/post")
    public String getShowBlogPost(HttpSession session, @RequestParam Long id, Model model) {
        String userName = (String) session.getAttribute("userName");
        if(userName == null){
            return "redirect:/";
        }

        Post post = postService.gePost(id);
        boolean isOwner = userName == post.getBlogUser().getName() ? true : false;

        model.addAttribute("isOwner", isOwner);
        model.addAttribute("post", post);
        return "post";
    }
    

    @PostMapping("/handleCreatePost")
    public String createPost(Post post) {
        postService.submitPost(post);
        //Not Secure
        return "redirect:/home?name="+post.getBlogUser().getName();
    }
    
    
    

}
