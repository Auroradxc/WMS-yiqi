package com.wcsb.wbs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wcsb.wbs.domain.User;
import com.wcsb.wbs.mapper.UserMapper;
import com.wcsb.wbs.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Jessica_Clorize
 * @since 2024-06-03
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    public Map<String, Object> Login(String username, String password){
        //System.out.println(username);
        //System.out.println(password);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", username);
        User user=userMapper.selectOne(queryWrapper);
        Map<String, Object> datamap=new HashMap<>();
        if(user.getPassword().equals(password)){
            datamap.put("HTTPCode",200);
            datamap.put("Data",user);
        }
        else{
            datamap.put("HTTPCode",500);
        }
        return datamap;
    }
}
