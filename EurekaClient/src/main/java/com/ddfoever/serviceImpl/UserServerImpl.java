package com.ddfoever.serviceImpl;

import com.ddfoever.User;
import com.ddfoever.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServerImpl implements UserService {

    private static Map<Integer,User> users ;

    static {
        users = new HashMap<>();
        users.put(1,new User(1,"aaa",11));
        users.put(2,new User(2,"bb",33));
        users.put(3,new User(3,"cc",22));
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User findById(int id) {
        return users.get(id);
    }

    @Override
    public void saveOrUpdate(User u) {
        users.put(u.getId(),u);
    }

    @Override
    public void deleteById(int id) {
        users.remove(id);
    }
}
