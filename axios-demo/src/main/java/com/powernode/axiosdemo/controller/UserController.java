package com.powernode.axiosdemo.controller;

import com.powernode.axiosdemo.model.User;
import org.springframework.web.bind.annotation.*;

// 接收跨域请求
@CrossOrigin
@RestController
public class UserController {

    @GetMapping("/user/query")
    public User getUser() {
        System.out.println("======/user/query  接收前端的请求... =========");
        User user = new User(1001, "zhangSan", 22, "male");
        return user;
    }

    // 两个参数
    @GetMapping("/user/get")
    public User queryUser(Integer id, String name) {
        System.out.println("======/user/get  接收前端的请求 ========= id=" + id +",name=" + name);
        User user = new User(id, name, 22, "male");
        return user;
    }


    // post请求
    @PostMapping("/user/add")
    public User addUser(Integer id, String name) {
        System.out.println("======/user/add  接收前端的请求 ========= id=" + id +",name=" + name);
        User user = new User(id, name, 22, "male");
        return user;
    }


    // 前端是json格式的数据 例如 {id: 1001, name: 'lisi'}， 需使用@RequestBody,从请求体中获取数据，并转为形参的对象
    @PostMapping("/user/json")
    public User addUserJson(@RequestBody User user) {
        System.out.println("======/user/json  接收前端的请求 ========= id=" +user);
        User user1 = new User(1001, "lisi", 22, "male");
        return user;
    }
}
