package com.yash.enotes.repository;

import com.yash.enotes.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {

    boolean existsByEmail(String email);

    User findByEmail(String username);

}
