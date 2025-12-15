package lu.cnfpc.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lu.cnfpc.blog.exception.BlogUserNotFoundException;
import lu.cnfpc.blog.model.BlogUser;
import lu.cnfpc.blog.repository.BlogUserRepository;

@Service
@Transactional
public class BlogUserService implements UserDetailsService{
    @Autowired
    private final BlogUserRepository userRepository;

    public BlogUserService(BlogUserRepository userRepository){
        this.userRepository = userRepository;
    }

    public void submitUser(BlogUser user){
        userRepository.save(user);
    }

    public BlogUser getUser(Long user_id){
        return userRepository.findById(user_id).orElseThrow(() -> new BlogUserNotFoundException("Customer not found with ID " + user_id));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        BlogUser blogUser = userRepository.findByName(username).orElseThrow(() -> new UsernameNotFoundException("The Bloguser with name " + username+" does not exist."));

        return User.withUsername(blogUser.getName()).password(blogUser.getPassword()).build();
    }

}
