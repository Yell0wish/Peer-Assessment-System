package com.ye.controller;

import com.ye.pojo.ClassPojo;
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

import java.util.HashMap;
import java.util.Map;

@RestController
public class PostController {
    @Autowired
    ClassService classService;

    @Autowired
    UserService userService;

    @Autowired
    TokenService tokenService;

    @Autowired
    SCService scService;

    @Autowired
    PostService postService;

    @RequestMapping(value = "/publishPost", method = RequestMethod.POST)
    public String publishPost(@RequestParam("userid") int userid,
                              @RequestParam("token") String token,
                              @RequestParam("classid") int classid,
                              @RequestParam("title") String title,
                              @RequestParam("content") String content,
                              @RequestParam("pic") String pic) {
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
                if (title.isEmpty()) {
                    return Result.defeat("标题不能为空");
                }
                if (content.isEmpty() && pic.isEmpty()) {
                    return Result.defeat("内容和图片不能同时为空");
                }
                postService.publishPost(classid, userid, title, content, pic, userService.getNameByID(userid));
                Map<String, Object> map = new HashMap<>();
                map.put("token", tokenService.getAndUpdateToken(userPojo.getEmail()));
                return Result.success("成功发布帖子", map);
            } else {
                return Result.defeat("您不在班级中");
            }
        }
    }

    @RequestMapping(value = "/getPostList", method = RequestMethod.GET)
    public String getPostList(@RequestParam("userid") int userid,
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
                Map<String, Object> map = new HashMap<>();
                map.put("token", tokenService.getAndUpdateToken(userPojo.getEmail()));
                map.put("postList", postService.getPostList(classid));
                return Result.success("成功获取帖子列表", map);
            } else {
                return Result.defeat("您不在班级中");
            }
        }
    }

    @RequestMapping(value = "/publishComment", method = RequestMethod.POST)
    public String publishComment(@RequestParam("userid") int userid,
                                 @RequestParam("token") String token,
                                 @RequestParam("classid") int classid,
                                 @RequestParam("postid") int postid,
                                 @RequestParam("content") String content,
                                 @RequestParam("pic") String pic) {
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
                // 判断帖子存不存在
                if (!postService.existPost(postid)) {
                    return Result.defeat("帖子不存在");
                }

                if (content.isEmpty() && pic.isEmpty()) {
                    return Result.defeat("内容和图片不能同时为空");
                }
                postService.publishComment(userid, postid, content, pic, userService.getNameByID(userid));
                Map<String, Object> map = new HashMap<>();
                map.put("token", tokenService.getAndUpdateToken(userPojo.getEmail()));
                return Result.success("成功跟帖", map);
            } else {
                return Result.defeat("您不在班级中");
            }
        }
    }

    @RequestMapping(value = "/getPostDetailsAndComments", method = RequestMethod.GET)
    public String getPostDetailsAndComments(@RequestParam("userid") int userid,
                                            @RequestParam("token") String token,
                                            @RequestParam("classid") int classid,
                                            @RequestParam("postid") int postid) {
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
                // 判断帖子存不存在
                if (!postService.existPost(postid)) {
                    return Result.defeat("帖子不存在");
                }
                Map<String, Object> map = new HashMap<>();
                map.put("token", tokenService.getAndUpdateToken(userPojo.getEmail()));
                map.put("postList", postService.getPostList(classid));
                map.put("postDetails", postService.getPostDetails(postid));
                map.put("postComments", postService.getPostComments(postid));
                return Result.success("成功获取帖子详情", map);
            } else {
                return Result.defeat("您不在班级中");
            }
        }
    }
}
