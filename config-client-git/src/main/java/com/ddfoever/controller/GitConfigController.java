package com.ddfoever.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gitConfig")
@RefreshScope
public class GitConfigController {
    @Value("${server.port}")
    private String port;

    @GetMapping("/index")
    public String index(){
        return  this.port;
    }
}
