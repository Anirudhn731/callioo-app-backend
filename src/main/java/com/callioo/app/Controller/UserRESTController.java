package com.callioo.app.Controller;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.callioo.app.Model.Users;
import com.callioo.app.Service.UserService;

import lombok.Data;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class UserRESTController {

    @Autowired
    private UserService userService;

    @GetMapping("/getAll")
    public List<Users> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/register")
    public boolean registerNewUser(@RequestBody Users newUser) {
        return userService.registerUser(newUser);
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest request) {
        System.out.println("LoginRequest Received with email :- " + request.getEmail());
        try {
            Users user = userService.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            if (!userService.encodedStringsmatches(request.getPassword(), user.getPassword())) {
                throw new RuntimeException("Invalid Password");
            }

            String token = userService.getJwtUtil().generateToken(user);

            return Map.of("status", "true", "token", token, "name", user.getFullName());
        } catch (Exception e) {
            return Map.of("status", "false", "message", e.getMessage());
        }
    }

    @GetMapping("/getUser")
    public Users getUser(@RequestParam @NonNull String email) {
        try {
            Users user = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("user not found"));
            user.setPassword("");

            return user;
        } catch (Exception e) {
            return null;
        }
    }

    @PutMapping("/updateUser")
    public String updateUser(@RequestBody Users user) {
        return userService.updateUser(user);
    }

    @DeleteMapping("/deleteUser")
    public boolean deleteUser(@RequestParam @NonNull String email) {
        return userService.deleteUser(email);
    }

    @GetMapping("/avatar")
    public ResponseEntity<Resource> getAvatar(@RequestParam String email) {
        String image_path = userService.findByEmail(email).get().getAvatarImage();
        Path path = Path.of(image_path);
        try {
            URI uri = path.toUri();
            Resource image;
            if (uri == null)
                throw new Exception("User avatar is empty");
            else
                image = new UrlResource(uri);

            String mimeType = Files.probeContentType(path);
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }

            return ResponseEntity.ok().contentType(MediaType.parseMediaType(mimeType)).body(image);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
}

@Data
class LoginRequest {
    private String email;
    private String password;

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
