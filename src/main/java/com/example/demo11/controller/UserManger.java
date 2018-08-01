package com.example.demo11.controller;

import com.example.demo11.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

@RestController
@Api(value = "/",description = "用户管理系统")
public class UserManger {

    @Autowired
    private SqlSessionTemplate template;

    @ApiOperation(value = "登录接口",httpMethod = "POST")
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public boolean  login(HttpServletResponse response, @RequestBody User user) {
        int i = template.selectOne("login",user);
        Cookie cookie = new Cookie("login","true");
        response.addCookie(cookie);
        if (i==1){
            return true;
        }
        return  false;
    }

    @ApiOperation( value="添加用户接口",httpMethod = "POST")
    @RequestMapping(value = "/adduser",method = RequestMethod.POST)
    public  boolean addUser(HttpServletRequest request,@RequestBody User user){

        Boolean x = verifCookies(request);
        int result = 0;
        if (x!=null){
            result = template.insert("addUser",user);
        }
        if (result>0){
            return true;
        }
        return  false;
    }

    private Boolean verifCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (Objects.isNull(cookies)) {
            return false;
        }
        for (Cookie  cookie: cookies){
            if (cookie.getName().equals("login")&&cookie.getValue().equals("true")){
                return true;
            }

        }
        return false;
    }

    @ApiOperation(value = "获取用户（列表）信息接口",httpMethod = "POST")
    @RequestMapping(value = "/getUserInfo",method = RequestMethod.POST)
    public List<User> getUserInfo(HttpServletRequest request,@RequestBody User user){
        Boolean x = verifCookies(request);
        if (x==true){
            List<User> users = template.selectList("getUserInfo",user);
            return  users;
        }else {
            return null;
        }
    }

    @ApiOperation(value = "更新删除用户接口",httpMethod = "POST")
    @RequestMapping(value = "/updateUser",method = RequestMethod.POST)
    public  int updateUser(HttpServletRequest request,@RequestBody User user){
        Boolean x = verifCookies(request);
        int i = 0;
        if(x==true){
            i=template.update("updateUserInfo",user);
        }
        return i;
    }

}
