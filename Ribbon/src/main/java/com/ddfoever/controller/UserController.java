package com.ddfoever.controller;

import com.ddfoever.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;

@RestController
@RequestMapping("/ribbon")
public class UserController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/users")
    public Collection<User> findAll(){

        return restTemplate.getForEntity("http://serverProvider/users/all",Collection.class).getBody();
    }


    @GetMapping("/index")
    public String getIndex(){
        return restTemplate.getForEntity("http://serverProvider/users/index",String.class).getBody();
    }
}
