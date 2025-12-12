package lu.cnfpc.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import lu.cnfpc.blog.service.BlogUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class BlogUserController {
    
    @Autowired 
    private final BlogUserService userService;

    public BlogUserController(BlogUserService userService){
        this.userService = userService;
    }

    @GetMapping("/")
    public String index() {
        return "index.html";
    }
    
}
