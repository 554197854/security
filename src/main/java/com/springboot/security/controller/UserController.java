package com.springboot.security.controller;

import com.springboot.security.bean.Msg;
import com.springboot.security.bean.User;
import com.springboot.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author N
 * @create 2018/12/13 -- 16:55
 */
@RestController
public class UserController {
    @Autowired
    UserService userService;

    //检查用户名
    @GetMapping("/checkUsername")
    public Msg checkUsername(String username){
        String regex="(^[a-zA-Z0-9]{4,16}$)|(^[\u2E80-\u9FFF]{2,5}$)";
        if(!username.matches(regex)){
            return Msg.fail().add("vn_msg", "用户名格式错误！");
        }
        boolean b = userService.checkUsername(username);
        if(b){
            return Msg.success().add("vn_msg", "用户名可用！");

        }else{
            return Msg.fail().add("vn_msg", "用户名不可用！");
        }
    }

    //检查邮箱
    @GetMapping("/checkEmail")
    public Msg checkEmail(String email){
        String regex="^([a-z0-9A-Z]+[-|\\\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\\\.)+[a-zA-Z]{2,}$";
        if(!email.matches(regex)){
            return Msg.fail().add("vm_msg", "邮箱格式错误！");
        }
        boolean b = userService.checkEmail(email);
        if(b){
            return Msg.success().add("vm_msg", "邮箱可用！");

        }else{
            return Msg.fail().add("vm_msg", "邮箱不可用！");
        }
    }

    //注册用户
    @PostMapping("/register")
    public Msg userRegister(@Valid User user, BindingResult bindingResult, HttpServletRequest request){//@Valid与BindingResult联合使用 验证提交User类的是否符合字段注解
        Map<String, Object> map =new HashMap<String, Object>();
        if(bindingResult.hasErrors()){
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for(FieldError fieldError:fieldErrors){
                map.put(fieldError.getField(),fieldError.getDefaultMessage());
            }
            return Msg.fail().add("error",map);//如果出现错误字段，添加进Msg中返回
        }else {
            userService.insertUser(user,request);
            return Msg.success();//如果没有错误，添加用户返回success
        }

    }
}