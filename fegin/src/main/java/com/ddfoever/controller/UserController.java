package com.ddfoever.controller;

import com.ddfoever.entity.User;
import com.ddfoever.feign.FeignInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/feign")
public class UserController {

    @Autowired
    private FeignInterface feignInterface;

    @GetMapping("/users")
    public Collection<User> findAll(){
        return feignInterface.findAll();
    }
    @GetMapping("/index")
    public String getIndex(){
        return feignInterface.index();
    }

}
