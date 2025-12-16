package lu.cnfpc.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lu.cnfpc.blog.exception.BlogUserNotFoundException;
import lu.cnfpc.blog.model.BlogUser;
import lu.cnfpc.blog.repository.BlogUserRepository;

@Service
@Transactional
public class BlogUserService {
    @Autowired
    private final BlogUserRepository userRepository;

    public BlogUserService(BlogUserRepository userRepository){
        this.userRepository = userRepository;
    }

    public void submitUser(BlogUser user){
        userRepository.save(user);
    }

    public BlogUser getUserById(Long userId){
        return userRepository.findById(userId).orElseThrow(() -> new BlogUserNotFoundException("Customer not found with ID " + userId));
    }

    public BlogUser getUserByName(String userName){
        return userRepository.findByName(userName);
    }

    public BlogUser loginUser(String name, String password){
        return userRepository.findByNameAndPassword(name, password);
    }

}
