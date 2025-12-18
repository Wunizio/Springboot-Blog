package lu.cnfpc.blog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import lu.cnfpc.blog.model.BlogUser;

@Repository
public interface BlogUserRepository extends JpaRepository<BlogUser, Long> {
    Optional<BlogUser> findByNameAndPassword(String name, String password);
    Optional<BlogUser> findByName(String name);
}
