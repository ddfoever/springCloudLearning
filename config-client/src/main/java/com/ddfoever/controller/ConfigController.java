package com.ddfoever.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/config")
public class ConfigController {

    @Value("${server.port}")
    private String port;
    @Value("${foo}")
    private String version;
    @GetMapping("/index")
    public String index(){
        return this.port +"--->" +this.version;
    }
}
