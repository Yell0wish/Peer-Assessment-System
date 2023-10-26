package com.ye.controller;


import com.ye.pojo.ClassInformationPublicPojo;
import com.ye.pojo.ClassPojo;
import com.ye.pojo.UserPojo;
import com.ye.service.ClassService;
import com.ye.service.SCService;
import com.ye.service.TokenService;
import com.ye.service.UserService;
import com.ye.utils.PasswordHash;
import com.ye.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class SCController {
    @Autowired
    UserService userService;

    @Autowired
    ClassService classService;

    @Autowired
    SCService scService;

    @Autowired
    TokenService tokenService;

    /**
     * 用户加入班级
     * 如果用户已加入班级或用户为班级拥有者则不会加入
     */
    @RequestMapping(value = "/attendCourse", method = RequestMethod.POST)
    public String attendCourse(@RequestParam("userid") int userid,
                               @RequestParam("token") String token,
                               @RequestParam("accessCode") String accessCode) {

        UserPojo userPojo = userService.selectByID(userid);
        if (userPojo == null) {
            return Result.defeat("用户ID不存在");
        } else if (!token.equals(tokenService.getToken(userPojo.getEmail()))) {
            return Result.defeat("token不正确");
        } else {
            ClassPojo classPojo = classService.selectClassByAccessCode(accessCode);
            if (classPojo == null) {
                return Result.defeat("请输入正确的邀请码");
            } else if (classPojo.getUserid() == userid) { //申请加入者就是拥有者
                return Result.defeat("您是班级拥有者");
            } else {
                if (scService.studentIsInclass(classPojo.getUuid(), userid)){ // 已经加入班级
                    return Result.defeat("您已在班级中");
                } else {
                    if (classPojo.getNum() >= classPojo.getMaxNum()) {
                        return Result.defeat("班级人数已满");
                    }
                    scService.attendClass(classPojo.getUuid(), userid);
                    Map<String, Object> map = new HashMap<>();
                    map.put("token", tokenService.getAndUpdateToken(userPojo.getEmail()));
                    return Result.success("您成功加入班级", map);
                }
            }
        }
    }

    /**
     *
     * @param userid
     * @param token
     * @param classid
     * @return 正确return您已成功退出班级 错误return 很多情况
     */
    // todo 退出班级群
    @RequestMapping(value = "/quitCourse", method = RequestMethod.DELETE)
    public String quitCourse(@RequestParam("userid") int userid,
                               @RequestParam("token") String token,
                               @RequestParam("classid") int classid) {

        UserPojo userPojo = userService.selectByID(userid);
        if (userPojo == null) {
            return Result.defeat("用户ID不存在");
        } else if (!token.equals(tokenService.getToken(userPojo.getEmail()))) {
            return Result.defeat("token不正确");
        } else {
            ClassPojo classPojo = classService.selectClassByID(classid);
            if (classPojo == null) {
                return Result.defeat("课程不存在");
            } else if (classPojo.getUserid() == userid) { //申请加入者就是拥有者
                return Result.defeat("您无法退出班级，您只能删除班级");
            } else {
                if (scService.studentIsInclass(classid, userid)){ // 已经加入班级
                    scService.quitCourse(classid, userid);
                    Map<String, Object> map = new HashMap<>();
                    map.put("token", tokenService.getAndUpdateToken(userPojo.getEmail()));
                    return Result.success("您已成功退出班级", map);
                } else {
                    return Result.defeat("您未在班级中");
                }
            }
        }
    }

    // 老师将学生移出班级
    @RequestMapping(value = "/removeFromClass", method = RequestMethod.DELETE)
    public String removeFromClass(@RequestParam("teacherid") int teacherid,
                             @RequestParam("token") String token,
                             @RequestParam("classid") int classid,
                                  @RequestParam("studentid") int studentid) {

        UserPojo userPojo = userService.selectByID(teacherid);
        if (userPojo == null) {
            return Result.defeat("老师用户ID不存在");
        } else if (!token.equals(tokenService.getToken(userPojo.getEmail()))) {
            return Result.defeat("老师token不正确");
        } else {
            ClassPojo classPojo = classService.selectClassByID(classid);
            if (classPojo == null) {
                return Result.defeat("课程不存在");
            } else if (classPojo.getUserid() == teacherid) { // 老师是课程拥有者
                if (scService.studentIsInclass(classid, studentid)){
                    scService.quitCourse(classid, studentid);
                    Map<String, Object> map = new HashMap<>();
                    map.put("token", tokenService.getAndUpdateToken(userPojo.getEmail()));
                    return Result.success("您已将学生移出班级", map);
                } else {
                    return Result.defeat("学生不在课程中");
                }
            } else {
                return Result.defeat("您不是课程拥有者");
            }
        }
    }


    // 老师获取学生信息
    @RequestMapping(value = "/getStudentInformation", method = RequestMethod.GET)
    public String getStudentInformation(@RequestParam("teacherid") int teacherid,
                                  @RequestParam("token") String token,
                                  @RequestParam("classid") int classid,
                                  @RequestParam("studentid") int studentid) {

        UserPojo userPojo = userService.selectByID(teacherid);
        if (userPojo == null) {
            return Result.defeat("老师用户ID不存在");
        } else if (!token.equals(tokenService.getToken(userPojo.getEmail()))) {
            return Result.defeat("老师token不正确");
        } else {
            ClassPojo classPojo = classService.selectClassByID(classid);
            if (classPojo == null) {
                return Result.defeat("课程不存在");
            } else if (classPojo.getUserid() == teacherid) {
                if (scService.studentIsInclass(classid, studentid)){
                    Map<String, Object> map = new HashMap<>();
                    map.put("information", userService.getPublicInformation(studentid));
                    map.put("token", tokenService.getAndUpdateToken(userPojo.getEmail()));
                    return Result.success("您已成功获取学生信息", map);
                } else {
                    return Result.defeat("学生不在课程中");
                }
            } else {
                return Result.defeat("您不是课程拥有者");
            }
        }
    }

    @RequestMapping(value = "/getAttendedCourse", method = RequestMethod.GET)
    public String getAttendedCourse(@RequestParam("userid") int userid,
                                    @RequestParam("token") String token) {

        UserPojo userPojo = userService.selectByID(userid);
        if (userPojo == null) {
            return Result.defeat("用户ID不存在");
        } else if (!token.equals(tokenService.getToken(userPojo.getEmail()))) {
            return Result.defeat("token不正确");
        } else {
            List<ClassInformationPublicPojo> attendedCoursePublcInformation = scService.getAttendedCoursePublcInformation(userid);
            Map<String, Object> map = new HashMap<>();
            map.put("attendedCoursePublcInformation", attendedCoursePublcInformation);
            map.put("token", tokenService.getAndUpdateToken(userPojo.getEmail()));
            return Result.success("已成功获取选课信息", map);
        }
    }

    @RequestMapping(value = "/getAttendeeList", method = RequestMethod.GET)
    public String getAttendeeList(@RequestParam("userid") int userid,
                                    @RequestParam("token") String token,
                                  @RequestParam("classid") int classid) {

        UserPojo userPojo = userService.selectByID(userid);
        if (userPojo == null) {
            return Result.defeat("用户ID不存在");
        } else if (!token.equals(tokenService.getToken(userPojo.getEmail()))) {
            return Result.defeat("token不正确");
        } else {
            ClassPojo classPojo = classService.selectClassByID(classid);
            if (classPojo == null) {
                return Result.defeat("课程不存在");
            }
            if (classPojo.getUserid() != userid && !scService.studentIsInclass(classid, userid)){
                return Result.defeat("不在课程中，无权访问");
            }
            Map<String, Object> map = new HashMap<>();
            map.put("token", tokenService.getAndUpdateToken(userPojo.getEmail()));
            map.put("attendeeList", scService.getAttendeeList(classid));
            return Result.success("已成功获取选课信息", map);
        }
    }


}

