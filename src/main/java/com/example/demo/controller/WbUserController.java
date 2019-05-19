package com.example.demo.controller;

import com.example.demo.mapper.WbUser;
import com.example.demo.mapper.WbUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/wbUserContrller")
public class WbUserController {
    @Autowired
    private WbUserMapper wbUserMappers;

    @RequestMapping("/getUserInfo")
    public void getUserInfo(@RequestParam(value = "user_id") Integer userId){
        WbUser wbuser = wbUserMappers.selectByPrimaryKey(userId);
    }
}
