package com.ye.controller;

import com.ye.pojo.UserPojo;
import com.ye.service.UserService;
import com.ye.utils.PasswordHash;
import com.ye.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String signup(@RequestParam("email") String email,
                         @RequestParam("password") String password) {

        // todo 有空加上邮件验证系统
        List<UserPojo> results = userService.selectByEmail(email);
        if (!results.isEmpty()) {
            return Result.defeat("邮箱已被占用");
        } else {
            userService.signup(email, password);
            return Result.success("注册成功");
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String password) {

        List<UserPojo> results = userService.selectByEmail(email);
        if (results.size() != 1) {
            // 防止邮箱未注册或者注册邮箱数大于1
            return Result.defeat("邮箱或密码错误");
        } else if (!PasswordHash.getInstance().getMD5(password).equals(results.get(0).getPassword())) {
            // 当密码不匹配的时候
            return Result.defeat("邮箱或密码错误");
        } else {
            // todo token加上时间限制
            Map<String, Object> map = new HashMap<>();
            map.put("userid", results.get(0).getUserid());
            map.put("token", PasswordHash.getInstance().getMD5(email));
            return Result.success("登录成功", map);
        }
    }

    @RequestMapping(value = "/getInformation/{userid}", method = RequestMethod.GET)
    public String getInformation(@PathVariable("userid") int userid,
                                 @RequestParam("token") String token) {
        UserPojo userPojo = userService.selectByID(userid);
        if (userPojo == null) {
            return Result.defeat("用户ID不存在");
        } else if (!token.equals(PasswordHash.getInstance().getMD5(userPojo.getEmail()))) {
            return Result.defeat("token不正确");
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("userInformation", userPojo);
            System.out.println(userPojo.getSchoolCode());
            return Result.success("成功获取信息", map);
        }
    }

    @RequestMapping(value = "/modifyName", method = RequestMethod.PUT)
    public String modifyName(@RequestParam("userid") int userid,
                             @RequestParam("token") String token,
                             @RequestParam("newName") String newName) {

        UserPojo userPojo = userService.selectByID(userid);
        if (userPojo == null) {
            return Result.defeat("用户ID不存在");
        } else if (!token.equals(PasswordHash.getInstance().getMD5(userPojo.getEmail()))) {
            return Result.defeat("token不正确");
        } else {
            // todo 用户名如果全是空格怎么办？
            if (newName.isEmpty()) {
                return Result.defeat("用户名不能为空");
            }
            userService.modifyName(userPojo, newName, userid);
            return Result.success("成功修改用户名");
        }
    }

    @RequestMapping(value = "/modifyPassword", method = RequestMethod.PUT)
    public String modifyPassword(@RequestParam("userid") int userid,
                                 @RequestParam("token") String token,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("oldPassword") String oldPassword) {

        UserPojo userPojo = userService.selectByID(userid);
        if (userPojo == null) {
            return Result.defeat("用户ID不存在");
        } else if (!token.equals(PasswordHash.getInstance().getMD5(userPojo.getEmail()))) {
            return Result.defeat("token不正确");
        } else if (!PasswordHash.getInstance().getMD5(oldPassword).equals(userPojo.getPassword())) {
            return Result.defeat("当前密码不正确");
        } else {
            userService.modifyPassword(userPojo, newPassword, userid);
            return Result.success("成功修改密码");
        }
    }

    @RequestMapping(value = "/modifyIdentity", method = RequestMethod.PUT)
    public String modifyIdentity(@RequestParam("userid") int userid,
                                 @RequestParam("token") String token,
                                 @RequestParam("newIdentity") int newIdentity) {

        UserPojo userPojo = userService.selectByID(userid);
        if (userPojo == null) {
            return Result.defeat("用户ID不存在");
        } else if (!token.equals(PasswordHash.getInstance().getMD5(userPojo.getEmail()))) {
            return Result.defeat("token不正确");
        } else {
            userService.modifyIdentity(userPojo, newIdentity, userid);
            return Result.success("成功修改身份");
        }
    }

    @RequestMapping(value = "/modifySchool", method = RequestMethod.PUT)
    public String modifySchool(@RequestParam("userid") int userid,
                               @RequestParam("token") String token,
                               @RequestParam("newSchool") String newSchool) {

        UserPojo userPojo = userService.selectByID(userid);
        if (userPojo == null) {
            return Result.defeat("用户ID不存在");
        } else if (!token.equals(PasswordHash.getInstance().getMD5(userPojo.getEmail()))) {
            return Result.defeat("token不正确");
        } else {
            userService.modifySchool(userPojo, newSchool, userid);
            return Result.success("成功修改学校");
        }
    }

    @RequestMapping(value = "/modifySchoolCode", method = RequestMethod.PUT)
    public String modifySchoolCode(@RequestParam("userid") int userid,
                                   @RequestParam("token") String token,
                                   @RequestParam("newSchoolCode") String newSchoolCode) {

        UserPojo userPojo = userService.selectByID(userid);
        if (userPojo == null) {
            return Result.defeat("用户ID不存在");
        } else if (!token.equals(PasswordHash.getInstance().getMD5(userPojo.getEmail()))) {
            return Result.defeat("token不正确");
        } else {
            userService.modifySchoolCode(userPojo, newSchoolCode, userid);
            return Result.success("成功修改学号");
        }
    }

    @RequestMapping(value = "/deleteUser", method = RequestMethod.DELETE)
    public String deleteUser(@RequestParam("userid") int userid,
                             @RequestParam("token") String token,
                             @RequestParam("password") String password) {

        UserPojo userPojo = userService.selectByID(userid);
        if (userPojo == null) {
            return Result.defeat("用户ID不存在");
        } else if (!token.equals(PasswordHash.getInstance().getMD5(userPojo.getEmail()))) {
            return Result.defeat("token不正确");
        } else if (!PasswordHash.getInstance().getMD5(password).equals(userPojo.getPassword())) {
            return Result.defeat("当前密码不正确");
        } else {
            userService.deleteUser(userid);
            return Result.success("用户已注销");
        }
    }


}
