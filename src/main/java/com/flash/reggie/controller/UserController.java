package com.flash.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.flash.reggie.common.R;
import com.flash.reggie.entity.User;
import com.flash.reggie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController{

    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    public R<User> login(HttpServletRequest request, @RequestBody User user){
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getPhone, user.getPhone());
        User userInfo = userService.getOne(userLambdaQueryWrapper);
        if(userInfo == null){
            userService.save(user);
            userInfo = userService.getOne(userLambdaQueryWrapper);
        }
        request.getSession().setAttribute("user", userInfo.getId());
        return R.success(userInfo);
    }

    @PostMapping("/loginout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("user");
        return R.success("用户退出");
    }

}
