package com.ddfoever.service;

import com.ddfoever.User;

import java.util.Collection;

public interface UserService {

    Collection<User> findAll();

    User findById(int id);

    void saveOrUpdate(User u);

    void deleteById(int id);
}
