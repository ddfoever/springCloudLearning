package com.ddfoever;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class RestTemplateApp {
    public static void main(String[] args) {
        SpringApplication.run(RestTemplateApp.class,args);
    }
    /**
     * create restTemplate to spring IOC container
     * 启动的时候就会创建一个RestTemplate 相当于用java 代码代替了xml 的配置
     * @Bean 此注解意思是将返回的bean 放入spring 容器中
     */
    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
