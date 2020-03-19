package com.example.service;

import com.example.mapper.UserMapper;
import com.example.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public void createOrUpdate(User user) {
        User dbUser = userMapper.findByAccountId(user.getAccountId());
        if (dbUser == null){
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtCreate(user.getGmtCreate());
            userMapper.insert(user);
        }else {
            dbUser.setAvatarUrl(user.getAvatarUrl());
            dbUser.setToken(user.getToken());
            dbUser.setName(user.getName());
            dbUser.setGmtModified(user.getGmtModified());
            userMapper.update(dbUser);
        }
    }

    public User findById(Integer commentator) {
        return userMapper.findById(commentator);
    }
}
