package lu.cnfpc.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import lu.cnfpc.blog.model.BlogUser;
import lu.cnfpc.blog.service.BlogUserService;

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

    public BlogUserController(BlogUserService userService){
        this.userService = userService;
    }

    @GetMapping("/")    
    public String getIndex(Model model, HttpSession session) {
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
