package com.Ecommerce.tubes_PBO.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.Ecommerce.tubes_PBO.model.User;
import com.Ecommerce.tubes_PBO.repo.UserRepo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
public class UserController {
    @Autowired
    UserRepo repo;

    @PostMapping("/addUser")
    public void addUser(@RequestBody User user) {
        repo.save(user);
    }
    @GetMapping("/allProducts")
    public String getMethodName() {
        return "berhasil diambil";
    }
    
}
