package com.callioo.app.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.callioo.app.Model.User;
import com.callioo.app.Repository.UserRepository;
import com.callioo.app.Security.JwtUtil;

@Service
public class UserService {
    private final String AVATAR_IMAGES_DIRECTORY = "src\\main\\resources\\static\\";

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public boolean registerUser(User newUser) {
        if (findByEmail(newUser.getEmail()).orElse(null) == null) {
            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
            if (newUser.getAvatarImage() == null || newUser.getAvatarImage() == "") {
                // setting default avatar
                newUser.setAvatarImage(AVATAR_IMAGES_DIRECTORY + "default-user-avatar.png");
            }
            userRepo.save(newUser);
            System.out.println("Successfully registered new user :- " + newUser.getEmail());
            return true;
        } else
            return false;
    }

    public Optional<User> findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public String updateUser(User updatedUser) {
        try {
            User user = findByEmail(updatedUser.getEmail()).orElseThrow(() -> new RuntimeException("user not found"));

            user.setFullName(updatedUser.getFullName());
            if (!updatedUser.getPassword().isEmpty())
                user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));

            userRepo.save(user);
        } catch (Exception e) {
            return e.getMessage();
        }

        return "Successfully Updated!";
    }

    public boolean deleteUser(String email) {
        if (email != null && email != "") {
            userRepo.deleteById(email);
            return true;
            // return "Successfully deleted profile!";
        } else
            return false;
        // return "There was a problem in deleting your profile!";
    }

    public boolean encodedStringsmatches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public JwtUtil getJwtUtil() {
        return this.jwtUtil;
    }
}
