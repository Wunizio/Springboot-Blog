package lu.cnfpc.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import lu.cnfpc.blog.model.BlogUser;
import lu.cnfpc.blog.service.BlogUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BlogUserController {
    
    @Autowired 
    private final BlogUserService userService;

    public BlogUserController(BlogUserService userService){
        this.userService = userService;
    }

    @GetMapping("/")
    public String getIndex(Model model) {
        model.addAttribute("blogUser", new BlogUser());
        return "index";
    }

    @GetMapping("/register")
    public String getRegister(Model model) {
        model.addAttribute("blogUser", new BlogUser());
        return "register";
    }
    

    @PostMapping("/handleRegister")
    public String registerBlogUser(BlogUser blogUser) {
        userService.submitUser(blogUser);
        return "redirect:/";
    }
    
    @PostMapping("/handleLogin")
    public String loginBlogUser(BlogUser attemptedUser, Model model) {
        BlogUser blogUser = userService.loginUser(attemptedUser.getName(), attemptedUser.getPassword());
        model.addAttribute("blogUser", blogUser);
        //Not Secure
        return "redirect:/home?name="+blogUser.getName();
    }

    
}
