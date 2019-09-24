package com.ddfoever.feign;

import com.ddfoever.entity.User;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class FeignInterfaceImpl implements FeignInterface {
    @Override
    public Collection<User> findAll() {
        return null;
    }

    @Override
    public String index() {
        return "服务器维护中。。。";
    }
}
