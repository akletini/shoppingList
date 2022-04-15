package com.akletini.shoppinglist.auth.controller;

import com.akletini.shoppinglist.auth.UserRepository;
import com.akletini.shoppinglist.auth.entity.User;
import com.akletini.shoppinglist.auth.service.EncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class AuthenticationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EncryptionService encryptionService;

    @PostMapping(value = "/createUser", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createUser(@RequestBody User user) {
        if (user == null) {
            return new ResponseEntity("Login failed", HttpStatus.BAD_REQUEST);
        }

        String hashedPassword = encryptionService.encryptPassword(user.getPassword());
        user.setPassword(hashedPassword);
        final User savedUser = userRepository.save(user);

        return ResponseEntity.ok().body(savedUser);
    }

    @PostMapping(value = "/loginUser", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity loginUser(@RequestBody User user) {
        if (user == null) {
            return new ResponseEntity("Invalid object", HttpStatus.BAD_REQUEST);
        }
        final User userFromDB = userRepository.findByUsername(user.getUsername());
        // if user exists
        if (userFromDB != null) {
            boolean correctPassword = encryptionService.arePasswordsEqual(userFromDB.getPassword(), user.getPassword());
            if (correctPassword) {
                userFromDB.setSignedIn(true);
                userFromDB.setStaySignedIn(user.getStaySignedIn());
                final User loggedInUser = userRepository.save(userFromDB);
                return ResponseEntity.ok().body(loggedInUser);
            }
        }
        return new ResponseEntity("Login failed", HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/logoutUser/{username}")
    public ResponseEntity<String> logoutUser(@PathVariable String username) {
        final User user = userRepository.findByUsername(username);
        if (user != null) {
            user.setSignedIn(false);
            return ResponseEntity.ok().body("User " + username + " logged out");
        }
        return ResponseEntity.badRequest().body("Found no user with username: " + username);
    }

    @GetMapping("/userExists/{username}")
    public ResponseEntity<String> userExists(@PathVariable String username) {
        final User user = userRepository.findByUsername(username);
        if (user != null) {
            return ResponseEntity.badRequest().body("User already exists");
        }
        return ResponseEntity.ok().body("Username is available");
    }

    @GetMapping(value = "/getUserByUsername/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        final User user = userRepository.findByUsername(username);
        if (user != null) {
            return ResponseEntity.ok().body(user);
        }
        return ResponseEntity.badRequest().body(null);
    }

    @GetMapping("/getUsers")
    public Iterable<User> getUsers() {
        return userRepository.findAll();
    }


}
