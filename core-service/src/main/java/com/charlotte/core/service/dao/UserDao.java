package com.charlotte.core.service.dao;

import com.charlotte.core.service.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserDao{

    User get(Integer id);

    List<User> findList();

//    @Insert("insert into user(id, name) values(#{id}, #{name})")
//    public int insert(User user);

}
