package lu.cnfpc.blog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import lu.cnfpc.blog.model.BlogUser;
import lu.cnfpc.blog.model.Follower;
import lu.cnfpc.blog.model.FollowerKey;
import lu.cnfpc.blog.model.Post;
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

import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class BlogUserController {
    
    @Autowired 
    private final BlogUserService userService;
    @Autowired
    private final PostService postService;
    @Autowired
    private final FollowerService followerService;

    public BlogUserController(BlogUserService userService, PostService postService, FollowerService followerService){
        this.userService = userService;
        this.postService = postService;
        this.followerService = followerService;
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
    public String getBlogUser(@RequestParam String userName, Model model, HttpSession session) {
        String sessionName = (String) session.getAttribute("userName");
        if(sessionName == null){
            return "redirect:/home";
        }

        BlogUser blogUserOwner = userService.getUserByName(session.getAttribute("userName").toString());
        BlogUser blogUser = userService.getUserByName(userName);

        List<BlogUser> followedUsers = followerService.findUsersFollowedByThisUser(blogUser);
        List<BlogUser> followers = followerService.findUsersFollowingThisUser(blogUser);

        List<Post> posts = postService.getAllPostByUser(blogUser);

        //Check if user is follwing the blogUser owner
        if (followerService.isUserAFollowingUserB(blogUserOwner, blogUser)){
            model.addAttribute("isFollowing", "true");
        }
        else{
            model.addAttribute("isFollowing", "false");
        }

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
        follower.setFollower(userService.getUserByName(session.getAttribute("userName").toString()));
        follower.setFollowing(userService.getUserByName(name));
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

        BlogUser blogUserOwner = userService.getUserByName(session.getAttribute("userName").toString());
        BlogUser blogUser = userService.getUserByName(name);

        Follower follower = followerService.findFollower(blogUserOwner, blogUser);
        followerService.removeFollower(follower);

        String redirectLink = "redirect:/blogUser?userName=";
        return redirectLink.concat(name);
    }
    
    
    

    @PostMapping("/handleRegister")
    public String registerBlogUser(BlogUser blogUser) {
        userService.submitUser(blogUser);
        return "redirect:/";
    }
    
    @PostMapping("/handleLogin")
    public String loginBlogUser(BlogUser attemptedUser, Model model, HttpSession session) {
        BlogUser blogUser = userService.loginUser(attemptedUser.getName(), attemptedUser.getPassword());
        model.addAttribute("blogUser", blogUser);
        session.setAttribute("userName", blogUser.getName());
        return "redirect:/";
    }

    
}
