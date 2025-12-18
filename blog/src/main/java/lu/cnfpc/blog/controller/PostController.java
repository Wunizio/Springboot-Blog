package lu.cnfpc.blog.controller;

import java.io.Console;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import lu.cnfpc.blog.exception.BlogUserNotFoundException;
import lu.cnfpc.blog.exception.PostNotFoundException;
import lu.cnfpc.blog.model.BlogUser;
import lu.cnfpc.blog.model.Category;
import lu.cnfpc.blog.model.Post;
import lu.cnfpc.blog.service.BlogUserService;
import lu.cnfpc.blog.service.CategoryService;
import lu.cnfpc.blog.service.FollowerService;
import lu.cnfpc.blog.service.PostService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;



@Controller
public class PostController {
    
    @Autowired
    private final PostService postService;
    @Autowired
    private final BlogUserService blogUserService;
    @Autowired
    private final CategoryService categoryService;
    @Autowired
    private final FollowerService followerService;

    public PostController(PostService postService, BlogUserService blogUserService, CategoryService categoryService, FollowerService followerService){
        this.postService = postService;
        this.blogUserService = blogUserService;
        this.categoryService = categoryService;
        this.followerService = followerService;
    }

    @GetMapping("/home")
    public String getHome(HttpSession session, Model model) {
        String userName = (String) session.getAttribute("userName");
        if(userName == null){
            return "redirect:/";
        }

        BlogUser blogUser = blogUserService.getUserByName(userName);
        List<BlogUser> followedUsers = followerService.findUsersFollowedByThisUser(blogUser);
        List<BlogUser> followers = followerService.findUsersFollowingThisUser(blogUser);
        model.addAttribute("blogUser", blogUser);
        model.addAttribute("posts", postService.getAllPostByUser(blogUser));
        model.addAttribute("followedUsers", followedUsers);
        model.addAttribute("followers", followers);
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

        List<Category> categories = categoryService.getAllCategories();

        model.addAttribute("categories", categories);
        model.addAttribute("post", post);
        return "createBlogpost";
    }

    @GetMapping("/post")
    public String getShowBlogPost(HttpSession session, @RequestParam Long id, Model model, RedirectAttributes redirectAttributes) {
        String userName = (String) session.getAttribute("userName");
        if(userName == null){
            return "redirect:/";
        }

        Post post;
        //Catch if user tries to access post that doesn't exist
        try{
            post = postService.gePost(id);
        }
        catch(PostNotFoundException e){
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/home";
        }

        model.addAttribute("post", post);
        return "post";
    }

    @GetMapping("/deletePost")
    public String deletePost(HttpSession session, @RequestParam Long id, Model model, RedirectAttributes redirectAttributes) {
        
        String sessionOwner = session.getAttribute("userName").toString();
        Post post;
        String postOwner;

        //Check if post exists
        try{
            post = postService.gePost(id);
        }
        catch(PostNotFoundException e){
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/home";
        }

        //Check if post owner exists
        try {
            postOwner = post.getBlogUser().getName();
        } catch (BlogUserNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/home";
        }

        //Check if owner of Post
        if(postOwner.equals(sessionOwner)){
            postService.deletePost(id);
        }
        return "redirect:/home";
    }

    @GetMapping("/search")
    public String getSearch(HttpSession session, Model model, @RequestParam(required = false) String property, @RequestParam(required = false) String order) {
        String userName = (String) session.getAttribute("userName");
        if(userName == null){
            return "redirect:/";
        }

        List<Post> posts;

        //Check if any Parameters are null
        if(property == null || order == null){
            posts = postService.getAllPostOrderByDateDesc();
        }
        else if(property.equals("title")){
            posts = order.equals("asc") ? postService.getAllPostOrderByTitleAsc() : postService.getAllPostOrderByTitleDesc();
        }
        else if(property.equals("author")){
            posts = order.equals("asc") ? postService.getAllPostOrderByAuthorAsc() : postService.getAllPostOrderByAuthorDesc();
        }
        else if(property.equals("category")){
            posts = order.equals("asc") ? postService.getAllPostOrderByCategoryAsc() : postService.getAllPostOrderByCategoryDesc();
        }
        else{
            posts = order.equals("asc") ? postService.getAllPostOrderByDateAsc() : postService.getAllPostOrderByDateDesc();
        }

        model.addAttribute("posts", posts);
        return "search";
    }
    
    

    @PostMapping("/handleCreatePost")
    public String createPost(@Valid Post post, BindingResult bindingResult, HttpSession session) {
        
        if(bindingResult.hasErrors()){
            return "createBlogpost";
        }

        //Set BlogUser in Post based on session attribute
        BlogUser blogOwner = blogUserService.getUserByName(session.getAttribute("userName").toString());
        post.setBlogUser(blogOwner);
        postService.submitPost(post);
        return "redirect:/home";
    }
    
    
    

}
