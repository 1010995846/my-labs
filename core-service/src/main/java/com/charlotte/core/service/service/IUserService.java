package com.charlotte.core.service.service;

import com.charlotte.core.service.entity.User;

import java.util.List;

public interface IUserService {
    List<User> findList();

    void register(User user);
}
