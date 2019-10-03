package com.inz.inz.repository;

import com.inz.inz.seciurity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
    User findByEmail(String email);
    Boolean existsByUsername(String username);
}
