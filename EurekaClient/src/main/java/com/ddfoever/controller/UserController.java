package com.ddfoever.controller;

import com.ddfoever.User;
import com.ddfoever.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {

    @Value("${server.port}")
    private String port;

    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public Collection<User> findAll(){
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public User find(@PathVariable("id") int id){
        return userService.findById(id);
    }

    @PostMapping
    public void save(@RequestBody User u){
        userService.saveOrUpdate(u);
    }
    @PutMapping
    public void update(@RequestBody User u){
        userService.saveOrUpdate(u);
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int id){
        userService.deleteById(id);
    }

    @GetMapping("/index")
    public String index(){
        return "当前端口 ："+this.port;
    }

}
