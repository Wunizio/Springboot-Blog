package lu.cnfpc.blog.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import lu.cnfpc.blog.service.BlogUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/index")
public class BlogUserController {
    
    @Autowired 
    private final BlogUserService userService;

    public BlogUserController(BlogUserService userService){
        this.userService = userService;
    }

    @GetMapping("/")
    public String index(Principal principal, Authentication auth, Model model) {
        System.out.println(auth.getPrincipal().toString());
        System.out.println(auth.getCredentials());
        System.out.println(auth.getName());
        System.out.println(auth.getDetails());
        System.out.println(auth.getAuthorities());
        System.out.println(auth.isAuthenticated());
        System.out.println("The principal is " +principal.getName());
        String userName = principal.getName();
        model.addAttribute("userName", userName);
        return "index.html";
    }

    @GetMapping("/customLogin")
    public String test(@RequestParam String param) {
        return "index.html";
    }
    
    
}
