package com.ye.controller;

import com.ye.pojo.UserPojo;
import com.ye.service.EmailService;
import com.ye.service.RedisService;
import com.ye.service.TokenService;
import com.ye.service.UserService;
import com.ye.utils.PasswordHash;
import com.ye.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    EmailService emailService;

    @Autowired
    RedisService redisService;

    @Autowired
    TokenService tokenService;

    /**
     * @param email
     * @param password
     * @param checkcode
     * @return
     */
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String signup(@RequestParam("email") String email,
                         @RequestParam("password") String password,
                         @RequestParam("checkcode") String checkcode) {

        UserPojo userPojo = userService.selectByEmail(email);
        if (userPojo != null) {
            return Result.defeat("邮箱已被占用");
        } else {
            String fromDatabase = (String) redisService.getAndDeleteFromDatabase(email + "signup");

            if (checkcode.equals(fromDatabase)) {
                userService.signup(email, password);
                return Result.success("注册成功");
            } else {
                return Result.defeat("验证码不正确");
            }

        }
    }

    /**
     * 注册给用户发邮件
     *
     * @param email
     * @return
     */

    @RequestMapping(value = "/signupCheckCode", method = RequestMethod.GET)
    public String signupCheckCode(@RequestParam("email") String email) {
        UserPojo userPojo = userService.selectByEmail(email);
        if (userPojo != null) {
            return Result.defeat("邮箱已被占用");
        } else {
            String checkcode = emailService.sendCheckEmail(email);
            redisService.saveIntoDatabase(email + "signup", checkcode, 10 * 60);
            return Result.success("已发送验证码");
        }
    }

    @RequestMapping(value = "/resetCheckCode", method = RequestMethod.GET)
    public String resetCheckCode(@RequestParam("email") String email) {
        UserPojo userPojo = userService.selectByEmail(email);
        if (userPojo == null) {
            return Result.defeat("邮箱未注册");
        } else {
            String checkcode = emailService.sendCheckEmail(email);
            redisService.saveIntoDatabase(email + "reset", checkcode, 10 * 60);
            return Result.success("已发送验证码");
        }
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String password) {

        UserPojo userPojo = userService.selectByEmail(email);
        if (userPojo == null) {
            // 防止邮箱未注册或者注册邮箱数大于1
            return Result.defeat("邮箱或密码错误");
        } else if (!PasswordHash.getInstance().getMD5(password).equals(userPojo.getPassword())) {
            // 当密码不匹配的时候
            return Result.defeat("邮箱或密码错误");
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("userid", userPojo.getUuid());
            map.put("token", tokenService.firstGetToken(email));
            return Result.success("登录成功", map);
        }
    }

    @RequestMapping(value = "/getInformation", method = RequestMethod.GET)
    public String getInformation(@RequestParam("userid") int userid,
                                 @RequestParam("token") String token) {
        UserPojo userPojo = userService.selectByID(userid);
        if (userPojo == null) {
            return Result.defeat("用户ID不存在");
        } else if (!token.equals(tokenService.getToken(userPojo.getEmail()))) {
            return Result.defeat("token不正确");
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("userInformation", userPojo);
            map.put("token", tokenService.getAndUpdateToken(userPojo.getEmail()));
//            System.out.println(userPojo.getSchoolCode());
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
        } else if (!token.equals(tokenService.getToken(userPojo.getEmail()))) {
            return Result.defeat("token不正确");
        } else {
            // todo 用户名如果全是空格怎么办？
            if (newName.isEmpty()) {
                return Result.defeat("用户名不能为空");
            }
            if (newName.equals(userPojo.getUsername())) {
                return Result.defeat("用户名不能与之前相同");
            }
            userService.modifyName(userPojo, newName, userid);
            Map<String, Object> map = new HashMap<>();
            map.put("token", tokenService.getAndUpdateToken(userPojo.getEmail()));
            return Result.success("成功修改用户名", map);
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
        } else if (!token.equals(tokenService.getToken(userPojo.getEmail()))) {
            return Result.defeat("token不正确");
        } else if (!PasswordHash.getInstance().getMD5(oldPassword).equals(userPojo.getPassword())) {
            return Result.defeat("当前密码不正确");
        } else {
            if (newPassword.equals(oldPassword)) {
                return Result.defeat("新旧密码不能一致");
            }

            userService.modifyPassword(userPojo, newPassword, userid);
            Map<String, Object> map = new HashMap<>();
            map.put("token", tokenService.getAndUpdateToken(userPojo.getEmail()));
            return Result.success("成功修改密码", map);
        }
    }

    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public String resetPassword(@RequestParam("email") String email,
                                @RequestParam("newPassword") String newPassword,
                                @RequestParam("checkcode") String checkcode) {

        UserPojo userPojo = userService.selectByEmail(email);
//        System.out.println(checkcode);
        if (userPojo == null) {
            return Result.defeat("用户ID不存在");
        } else if (!checkcode.equals(redisService.getAndDeleteFromDatabase(email + "reset"))) {
            return Result.defeat("验证码不正确");
        } else if (PasswordHash.getInstance().getMD5(newPassword).equals(userPojo.getPassword())) {
            return Result.defeat("新旧密码不能一致");
        } else {
            userService.modifyPassword(userPojo, newPassword, email);
            return Result.success("成功重置密码");
        }
    }

    @RequestMapping(value = "/modifyIdentity", method = RequestMethod.PUT)
    public String modifyIdentity(@RequestParam("userid") int userid,
                                 @RequestParam("token") String token,
                                 @RequestParam("newIdentity") int newIdentity) {

        UserPojo userPojo = userService.selectByID(userid);
        if (userPojo == null) {
            return Result.defeat("用户ID不存在");
        } else if (!token.equals(tokenService.getToken(userPojo.getEmail()))) {
            return Result.defeat("token不正确");
        } else {
            if (newIdentity != 1 && newIdentity != 2 && newIdentity != 3) {
                return Result.defeat("新身份不合法");
            }

            if (newIdentity == userPojo.getIdentity()){
                return Result.defeat("新旧身份不能一致");
            }
            userService.modifyIdentity(userPojo, newIdentity, userid);
            Map<String, Object> map = new HashMap<>();
            map.put("token", tokenService.getAndUpdateToken(userPojo.getEmail()));
            return Result.success("成功修改身份", map);
        }
    }

    @RequestMapping(value = "/modifySchool", method = RequestMethod.PUT)
    public String modifySchool(@RequestParam("userid") int userid,
                               @RequestParam("token") String token,
                               @RequestParam("newSchool") String newSchool) {

        UserPojo userPojo = userService.selectByID(userid);
        if (userPojo == null) {
            return Result.defeat("用户ID不存在");
        } else if (!token.equals(tokenService.getToken(userPojo.getEmail()))) {
            return Result.defeat("token不正确");
        } else {
            if (newSchool.equals(userPojo.getSchool())){
                return Result.defeat("新旧学校不能一致");
            }

            userService.modifySchool(userPojo, newSchool, userid);
            Map<String, Object> map = new HashMap<>();
            map.put("token", tokenService.getAndUpdateToken(userPojo.getEmail()));
            return Result.success("成功修改学校", map);
        }
    }

    @RequestMapping(value = "/modifySchoolCode", method = RequestMethod.PUT)
    public String modifySchoolCode(@RequestParam("userid") int userid,
                                   @RequestParam("token") String token,
                                   @RequestParam("newSchoolCode") String newSchoolCode) {

        UserPojo userPojo = userService.selectByID(userid);
        if (userPojo == null) {
            return Result.defeat("用户ID不存在");
        } else if (!token.equals(tokenService.getToken(userPojo.getEmail()))) {
            return Result.defeat("token不正确");
        } else {
            if (newSchoolCode.equals(userPojo.getSchoolCode())){
                return Result.defeat("新旧学校不能一致");
            }

            Map<String, Object> map = new HashMap<>();
            map.put("token", tokenService.getAndUpdateToken(userPojo.getEmail()));

            userService.modifySchoolCode(userPojo, newSchoolCode, userid);
            return Result.success("成功修改学号", map);
        }
    }

    @RequestMapping(value = "/deleteUser", method = RequestMethod.DELETE)
    public String deleteUser(@RequestParam("userid") int userid,
                             @RequestParam("token") String token,
                             @RequestParam("password") String password) {

        UserPojo userPojo = userService.selectByID(userid);
        if (userPojo == null) {
            return Result.defeat("用户ID不存在");
        } else if (!token.equals(tokenService.getToken(userPojo.getEmail()))) {
            return Result.defeat("token不正确");
        } else if (!PasswordHash.getInstance().getMD5(password).equals(userPojo.getPassword())) {
            return Result.defeat("当前密码不正确");
        } else {
            userService.deleteUser(userid);
            return Result.success("用户已注销");
        }
    }

    // 增加用户头像

}
