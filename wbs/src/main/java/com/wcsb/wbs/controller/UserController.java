package com.wcsb.wbs.controller;


import com.wcsb.wbs.domain.User;
import com.wcsb.wbs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Jessica_Clorize
 * @since 2024-06-03
 */
@RestController
//@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public String LoginUser(@RequestBody User data) {
        String Name=data.getName();
        String Password=data.getPassword();
        Map<String, Object> Map=userService.Login(Name,Password);
        int code=new Integer(String.valueOf(Map.get("HTTPCode")));
        if(code==200){
            System.out.println(data.getName());
            System.out.println(data.getPassword());
            return "Login Success";
        }
        else{
            System.out.println("Login Failed!");
            return "Login Failed";
        }
    }
}

