package com.yash.enotes.service;

import com.yash.enotes.entity.CustomUserDetails;
import com.yash.enotes.entity.User;
import com.yash.enotes.service.IUserService.UserService;
import com.yash.enotes.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepo, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.encoder = encoder;
    }
    @Override
    public User getUser(Integer id) {
        return userRepo.findById(id).orElse(null);
    }

    @Override
    public User createUser(User user){
        //check if the same email exists or not
        if(userRepo.existsByEmail(user.getEmail()))throw new IllegalArgumentException("email already exists!");
        user.setPassword(encoder.encode(user.getPassword()));
        user.setActive(true);
        user.setRole("ROLE_USER");
        user.setCreatedAt(LocalDateTime.now());
        return userRepo.save(user);
    }

    @Override
    public boolean updateUser(User user) {
        User existingUser = getUser(user.getId());
        //checking if the user exists
        if (existingUser == null) throw new IllegalArgumentException("no such user exists!");

        try {
            //if user exists update the existing user
            existingUser.setName(user.getName());
            existingUser.setAddress(user.getAddress());
            existingUser.setGender(user.getGender());

            userRepo.save(existingUser);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    @Override
    public boolean deleteUser(Integer id) {
        User existingUser = getUser(id);
        //checking if the user exists
        if (existingUser == null) throw new IllegalArgumentException("no such user exists!");

        try {
            //if user exists delete it
            existingUser.setActive(false);
            userRepo.save(existingUser);
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public User getLoggedInUser(HttpSession session) {
        System.out.println("getLoggedInUser ");
        CustomUserDetails userDet = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User currentUser = userRepo.findByEmail(userDet.getUsername());
        System.out.print(currentUser);
        session.setAttribute("currentUser",currentUser);
        return currentUser;
    }
}
