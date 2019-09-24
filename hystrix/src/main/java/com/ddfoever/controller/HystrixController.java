package com.ddfoever.controller;

import com.ddfoever.entity.User;
import com.ddfoever.feign.FeignInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/hystrix")
public class HystrixController {

    @Autowired
    private FeignInterface feignInterface;
    @GetMapping("/users")
    public Collection<User> getAll(){
        return feignInterface.findAll();
    }
}
