package com.charlotte.core.service.service.impl;

import com.charlotte.core.service.dao.UserDao;
import com.charlotte.core.service.entity.User;
import com.charlotte.core.service.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
public class UserService implements IUserService /*UserDetailsService*/ {

    @Autowired
    private UserDao userDao;

//    @Transactional(readOnly = false, rollbackFor = RuntimeException.class)
//    public User add(User user){
//        user = userDao.insert(user);
//        return user;
//    }

    @Override
    public List<User> findList(){
//        List<User> userList = userDao.findList();
        List<User> userList = new ArrayList<>();
        userList.add(new User("01"));
        return userList;
    }

//    @Override
//    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
////        userDao.getByName(){}
//        return new User();
//    }
}
