package com.akletini.shoppinglist.auth.controller;

import com.akletini.shoppinglist.auth.UserRepository;
import com.akletini.shoppinglist.auth.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class AuthenticationController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/saveUser")
    public void persistUser(@RequestBody User user) {
        userRepository.save(user);
    }

    @GetMapping("/getUsers")
    public Iterable<User> getUsers(){
        return userRepository.findAll();
    }


}
