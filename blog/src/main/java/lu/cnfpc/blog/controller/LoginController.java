package lu.cnfpc.blog.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import lu.cnfpc.blog.model.BlogUser;
import lu.cnfpc.blog.service.BlogUserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
public class LoginController {
    
     @Autowired 
    private final BlogUserService blogUserService;

    public LoginController(BlogUserService blogUserService){
        this.blogUserService = blogUserService;
    }


    @GetMapping("/login")
    public String customLogin(Model model) {
        model.addAttribute("blogUser", new BlogUser());
        return "login";
    }

    @PostMapping("/handleRegister")
    public String handleRegister(@Valid BlogUser blogUser) {
        blogUserService.submitUser(blogUser);
        
        return "redirect:/login";
    }
    
    

    /*@PostMapping("/handleLogin")
    public String postMethodName(Principal principal, Authentication auth, Model model) {
        System.out.println(auth.getPrincipal().toString());
        System.out.println(auth.getCredentials());
        System.out.println(auth.getName());
        System.out.println(auth.getDetails());
        System.out.println(auth.getAuthorities());
        System.out.println(auth.isAuthenticated());
        System.out.println("The principal is " +principal.getName());
        String userName = principal.getName();
        model.addAttribute("userName", userName);
        return "redirect:/main";
    }*/
    
    

}
