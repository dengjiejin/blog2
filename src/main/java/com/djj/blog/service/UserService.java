package com.djj.blog.service;

import com.djj.blog.dao.UserDao;
import com.djj.blog.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by wangchenghao on 2017/8/2.
 */
@Service
public class UserService {

    @Resource
    UserDao userDao;

    public User findUserByUsername(String username){
        return userDao.findUserByUsername(username);
    }

    public boolean login(String username, String password){
        User user = userDao.findByUsernameAndPassword(username, password);
        if(null == user){

            return false;
        }else {

            return true;
        }
    }
}
