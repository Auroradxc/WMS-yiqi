package com.wcsb.wbs.service;

import com.wcsb.wbs.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wcsb.wbs.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Jessica_Clorize
 * @since 2024-06-03
 */
public interface UserService extends IService<User> {
    Map<String, Object> Login(String username, String password);
}
