package lu.cnfpc.blog.controller;

import java.io.Console;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import lu.cnfpc.blog.model.BlogUser;
import lu.cnfpc.blog.model.Category;
import lu.cnfpc.blog.model.Post;
import lu.cnfpc.blog.service.BlogUserService;
import lu.cnfpc.blog.service.CategoryService;
import lu.cnfpc.blog.service.PostService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;



@Controller
public class PostController {
    
    @Autowired
    private final PostService postService;
    @Autowired
    private final BlogUserService blogUserService;
    @Autowired
    private final CategoryService categoryService;

    public PostController(PostService postService, BlogUserService blogUserService, CategoryService categoryService){
        this.postService = postService;
        this.blogUserService = blogUserService;
        this.categoryService = categoryService;
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

        List<Category> categories = categoryService.getAllCategories();

        model.addAttribute("categories", categories);
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
        model.addAttribute("post", post);
        return "post";
    }

    @GetMapping("/deletePost")
    public String deletePost(HttpSession session, @RequestParam Long id, Model model) {
        //Check if owner of Post
        String postOwner = postService.gePost(id).getBlogUser().getName();
        String sessionOwner = session.getAttribute("userName").toString();
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
    public String createPost(Post post, HttpSession session) {
        //Set BlogUser in Post based on session attribute
        BlogUser blogOwner = blogUserService.getUserByName(session.getAttribute("userName").toString());
        post.setBlogUser(blogOwner);
        postService.submitPost(post);
        return "redirect:/home";
    }
    
    
    

}
