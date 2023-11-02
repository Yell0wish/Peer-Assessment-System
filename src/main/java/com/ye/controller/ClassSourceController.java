package com.ye.controller;

import com.ye.pojo.ClassPojo;
import com.ye.pojo.ClassSourcePojo;
import com.ye.pojo.FilePojo;
import com.ye.pojo.UserPojo;
import com.ye.service.*;
import com.ye.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ClassSourceController {
    @Autowired
    ClassService classService;

    @Autowired
    UserService userService;

    @Autowired
    ClassSourceService classSourceService;

    @Autowired
    TokenService tokenService;

    @Autowired
    SCService scService;

    @RequestMapping(value = "/addClassSource", method = RequestMethod.POST)
    public String addClassSource(@RequestParam("userid") int userid,
                                 @RequestParam("token") String token,
                                 @RequestParam("classid") int classid,
                                 @RequestParam("sourceName") String sourceName,
                                 @RequestParam("attachment") MultipartFile attachment) {

        UserPojo userPojo = userService.selectByID(userid);
        if (userPojo == null) {
            return Result.defeat("用户ID不存在");
        } else if (!token.equals(tokenService.getToken(userPojo.getEmail()))) {
            return Result.defeat("token不正确");
        } else {
            ClassPojo classPojo = classService.selectClassByID(classid);
            if (classPojo == null) {
                return Result.defeat("课程不存在");
            } else if (classPojo.getUserid() == userid) { //正常操作
                if (sourceName.isEmpty()) {
                    return Result.defeat("文件名不能为空");
                }

                if (attachment.isEmpty()) {
                    return Result.defeat("文件不能为空");
                }
                classSourceService.addClassSource(classid, sourceName, attachment);
                Map<String, Object> map = new HashMap<>();
                map.put("token", tokenService.getAndUpdateToken(userPojo.getEmail()));
                return Result.success("您已成功添加资源", map);
            } else {
                return Result.defeat("您不是班级拥有者");
            }
        }
    }



    @RequestMapping(value = "/getResource", method = RequestMethod.GET)
    public String getResource(HttpServletResponse response,
                              @RequestParam("userid") int userid,
                              @RequestParam("token") String token,
                              @RequestParam("classid") int classid,
                              @RequestParam("sourceid") int sourceid) throws IOException {

        UserPojo userPojo = userService.selectByID(userid);
        if (userPojo == null) {
            return Result.defeat("用户ID不存在");
        } else if (!token.equals(tokenService.getToken(userPojo.getEmail()))) {
            return Result.defeat("token不正确");
        } else {
            ClassPojo classPojo = classService.selectClassByID(classid);
            if (classPojo == null) {
                return Result.defeat("课程不存在");
            } else if (classPojo.getUserid() == userid || scService.studentIsInclass(classid, userid)) { // 正常操作
                FilePojo resource = classSourceService.getResource(sourceid);
                if (resource != null && resource.getFile() != null) {
                    return Result.success(resource.getFileName(), resource.getFile(), response);
                } else {
                    return Result.defeat("无对应资源内容");
                }
            } else {
                return Result.defeat("您不在班级中");
            }
        }
    }

    @RequestMapping(value = "/getResourceList", method = RequestMethod.GET)
    public String getResourceList(HttpServletResponse response,
                              @RequestParam("userid") int userid,
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
            } else if (classPojo.getUserid() == userid || scService.studentIsInclass(classid, userid)) { // 正常操作
                List<ClassSourcePojo> resourceList = classSourceService.getResourceList(classid);
                Map<String, Object> map = new HashMap<>();
                map.put("resourceList", resourceList);
                map.put("token", tokenService.getAndUpdateToken(userPojo.getEmail()));
                return Result.success("您已获取资源列表", map);
            } else {
                return Result.defeat("您不在班级中");
            }
        }
    }
}
