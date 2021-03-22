package com.charlotte.core.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.charlotte.core.service.enums.BusinessExceptionEnum;
import com.charlotte.core.service.mapper.UserMapper;
import com.charlotte.core.service.entity.User;
import com.charlotte.core.service.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.function.Supplier;

//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
@Transactional(rollbackFor = Throwable.class)
public class UserService implements IUserService /*UserDetailsService*/ {

    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private UserMapper userMapper;

    @Override
    public void register(User user){
        publisher.publishEvent(user);
        publisher.publishEvent(user);
        Supplier function = () -> {
            System.out.println("发送成功");
            return user;
        };
        publisher.publishEvent(function);
        BusinessExceptionEnum.PARAM_ERROR.nonNull(user.getName(), "用户名");
        user.setId(null);
        userMapper.insert(user);
    }

    @Override
    public List<User> findList(){
        List<User> list = userMapper.selectList(new QueryWrapper<>());
        return list;
    }

//    @Override
//    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
////        userDao.getByName(){}
//        return new User();
//    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void afterRollback(User user){
        System.out.println(user.getName());
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void beforeCommit(Supplier function){
        System.out.println(function.get());
        return;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void afterCommit(User user){
        System.out.println(user.getName());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    public void afterCompletion(User user){
        System.out.println(user.getName());
    }
}
