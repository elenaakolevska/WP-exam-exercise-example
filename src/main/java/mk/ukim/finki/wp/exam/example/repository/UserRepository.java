package mk.ukim.finki.wp.exam.example.repository;

import mk.ukim.finki.wp.exam.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUsername(String username);
}
