package lu.cnfpc.blog.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import lu.cnfpc.blog.exception.BlogUserNotFoundException;
import lu.cnfpc.blog.exception.FollowerNotFoundException;
import lu.cnfpc.blog.model.BlogUser;
import lu.cnfpc.blog.model.Follower;
import lu.cnfpc.blog.model.FollowerKey;
import lu.cnfpc.blog.model.Post;
import lu.cnfpc.blog.repository.BlogUserRepository;
import lu.cnfpc.blog.service.BlogUserService;
import lu.cnfpc.blog.service.FollowerService;
import lu.cnfpc.blog.service.PostService;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class BlogUserController {

    private final BlogUserRepository blogUserRepository;
    
    @Autowired 
    private final BlogUserService userService;
    @Autowired
    private final PostService postService;
    @Autowired
    private final FollowerService followerService;

    public BlogUserController(BlogUserService userService, PostService postService, FollowerService followerService, BlogUserRepository blogUserRepository){
        this.userService = userService;
        this.postService = postService;
        this.followerService = followerService;
        this.blogUserRepository = blogUserRepository;
    }

    @GetMapping("/")    
    public String getIndex(Model model, HttpSession session) {
        //If session attribute exists, return to homepage
        String userName = (String) session.getAttribute("userName");
        if(userName != null){
            return "redirect:/home";
        }
        
        //Add dummy Bloguser for the login form
        model.addAttribute("blogUser", new BlogUser());
        return "index";
    }

    @GetMapping("/register")
    public String getRegister(Model model) {
        model.addAttribute("blogUser", new BlogUser());
        return "register";
    }

    @GetMapping("/logout")
    public String getLogout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/blogUser")
    public String getBlogUser(@RequestParam String userName, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        String sessionName = (String) session.getAttribute("userName");
        if(sessionName == null){
            return "redirect:/home";
        }

        //Set Bloguser from page and BloguserSessionOwner to the Loged in user
        BlogUser blogUserSessionOwner = userService.getUserByName(session.getAttribute("userName").toString());
        BlogUser blogUser;

        //Check if userName in RequestParam exists
        try{
            blogUser = userService.getUserByName(userName);
        }
        catch(BlogUserNotFoundException e){
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/home";
        }

        //Set the Followers and Followed Users
        List<BlogUser> followedUsers = followerService.findUsersFollowedByThisUser(blogUser);
        List<BlogUser> followers = followerService.findUsersFollowingThisUser(blogUser);

        List<Post> posts = postService.getAllPostByUser(blogUser);

        //If it catches exception, UserA is not following UserB OR UserA is checking out their own Page
        boolean isFollowing;
        try{
            followerService.isUserAFollowingUserB(blogUserSessionOwner, blogUser);
            isFollowing = true;
        }
        catch(FollowerNotFoundException e){
            isFollowing = false;
        }

        model.addAttribute("isFollowing", isFollowing);
        model.addAttribute("blogUser", blogUser);
        model.addAttribute("posts", posts);
        model.addAttribute("followedUsers", followedUsers);
        model.addAttribute("followers", followers);

        return "blogUser";  
    }

    @GetMapping("/followUser")
    public String getFollowUser(@RequestParam String name, HttpSession session) {
        String sessionName = (String) session.getAttribute("userName");
        if(sessionName == null){
            return "redirect:/home";
        }

        //Create new Follower Object and set attributes
        Follower follower = new Follower();

        //Redirect if Username does not exist anymore
        try{
            follower.setFollower(userService.getUserByName(session.getAttribute("userName").toString()));
            follower.setFollowing(userService.getUserByName(name));
        }
        catch(BlogUserNotFoundException e){
            return "redirect:/";
        }
       
        follower.setFollowerId(new FollowerKey());
        followerService.submitFollower(follower);

        String redirectLink = "redirect:/blogUser?userName=";
        return redirectLink.concat(name);
    }

    @GetMapping("/unfollowUser")
    public String getUnfollowUser(@RequestParam String name, HttpSession session) {
        String sessionName = (String) session.getAttribute("userName");
        if(sessionName == null){
            return "redirect:/home";
        }

        BlogUser blogUserOwner;
        BlogUser blogUser;
        
        //Redirect if Username does not exist anymore
        try{
            blogUserOwner = userService.getUserByName(session.getAttribute("userName").toString());
            blogUser = userService.getUserByName(name);

        }
        catch(BlogUserNotFoundException e){
            return "redirect:/";
        }

        Follower follower;
        //Catch any errors in finding the Follower association
        try{
            follower = followerService.findFollower(blogUserOwner, blogUser);
        }
        catch(FollowerNotFoundException e){
            return "redirect:/";
        }

        followerService.removeFollower(follower);

        String redirectLink = "redirect:/blogUser?userName=";
        return redirectLink.concat(name);
    }


    @PostMapping("/handleRegister")
    public String registerBlogUser(@Valid BlogUser blogUser, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()){
            return "register";
        }

        //Check if Username is taken
        try{
            userService.getUserByName(blogUser.getName());
        }
        catch(BlogUserNotFoundException e){
            userService.submitUser(blogUser);
            return "redirect:/";
        }
        model.addAttribute("message", "Username is already taken");
        return "register";

        
    }
    
    @PostMapping("/handleLogin")
    public String loginBlogUser(BlogUser attemptedUser, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        
        BlogUser blogUser;
        //Catch wrong login credentials
        try{
            blogUser = userService.loginUser(attemptedUser.getName(), attemptedUser.getPassword());
        }
        catch(BlogUserNotFoundException e){
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/";
        }
        
        //Save LastLogin
        blogUser.setLastLogin(LocalDateTime.now());
        userService.submitUser(blogUser);

        model.addAttribute("blogUser", blogUser);
        session.setAttribute("userName", blogUser.getName());
        return "redirect:/";
    }

    
}
