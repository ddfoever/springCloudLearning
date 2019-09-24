package com.ddfoever.feign;

import com.ddfoever.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;

@FeignClient(value = "serverProvider",fallback = FeignInterfaceImpl.class)
public interface FeignInterface {
    @GetMapping("/users/all")
    public Collection<User> findAll();
    @GetMapping("/users/index")
    public String index();
}
