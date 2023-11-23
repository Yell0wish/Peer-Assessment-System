package com.ye.controller;

import com.ye.pojo.*;
import com.ye.service.*;
import com.ye.utils.Result;
import org.apache.el.parser.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.feed.AbstractRssFeedView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class HomeworkController {
    @Autowired
    HomeworkService homeworkService;

    @Autowired
    UserService userService;

    @Autowired
    TokenService tokenService;

    @Autowired
    ClassService classService;

    @Autowired
    SCService scService;

    @RequestMapping(value = "/addHomework", method = RequestMethod.POST)
    public String addHomework(@RequestParam("userid") int userid,
                              @RequestParam("token") String token,
                              @RequestParam("classid") int classid,
                              @RequestParam("title") String title,
                              @RequestParam("content") String content,
                              @RequestParam("attachmentName") String attachmentName,
                              @RequestParam("attachment") MultipartFile attachment,
                              @RequestParam("submitTimeString") String submitTimeString,
                              @RequestParam("default_score") double default_score) {

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
                if (title.isEmpty()) {
                    return Result.defeat("作业名称不能为空");
                }
                if (!attachment.isEmpty() && attachmentName.isEmpty()) {
                    return Result.defeat("附件名不能为空");
                }
                if (!attachmentName.isEmpty() && attachment.isEmpty()) {
                    return Result.defeat("附件内容不能为空");
                }
                if (attachmentName.isEmpty() && content.isEmpty()) {
                    return Result.defeat("附件和内容不能同时为空");
                }


                // 进行日期格式转换
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                sdf.setLenient(false);
                Date submitTime = null;
                try {
                    submitTime = sdf.parse(submitTimeString);
                } catch (Exception e) {
                    return Result.defeat("日期格式不正确，应为 yyyy-MM-dd HH:mm:ss");
                }
                ZoneId zoneId = ZoneId.of("Asia/Shanghai"); // 指定"Asia/Shanghai"时区（北京时间）
                Date date = Date.from(LocalDateTime.now(zoneId).atZone(zoneId).toInstant());// 获取当前时间
                if (date.compareTo(submitTime) >= 0) {
                    return Result.defeat("截至时间设置不正确");
                }

                if(default_score < 0 || default_score > 100){
                    return Result.defeat("默认分数不正确");
                }

                homeworkService.addHomework(classid, title, content, attachmentName, attachment, submitTime, default_score);
                Map<String, Object> map = new HashMap<>();
                map.put("token", tokenService.getAndUpdateToken(userPojo.getEmail()));
                return Result.success("您已成功布置作业", map);
            } else {
                return Result.defeat("您不是班级拥有者");
            }
        }
    }

    @RequestMapping(value = "/getHomeworkList", method = RequestMethod.GET)
    public String getHomeworkList(@RequestParam("userid") int userid,
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
            if (classPojo.getUserid() != userid && !scService.studentIsInclass(classid, userid)) {
                return Result.defeat("不在课程中，无权访问");
            }
            Map<String, Object> map = new HashMap<>();
            map.put("token", tokenService.getAndUpdateToken(userPojo.getEmail()));
            map.put("homeworkList", homeworkService.getHomeworkList(classid));
            return Result.success("已成功获取作业列表", map);
        }
    }

    @RequestMapping(value = "/getHomeworkDetails", method = RequestMethod.GET)
    public String getHomeworkDetails(@RequestParam("userid") int userid,
                                     @RequestParam("token") String token,
                                     @RequestParam("classid") int classid,
                                     @RequestParam("homeworkid") int homeworkid) {

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
            if (classPojo.getUserid() != userid && !scService.studentIsInclass(classid, userid)) {
                return Result.defeat("不在课程中，无权访问");
            }
            Map<String, Object> map = new HashMap<>();
            map.put("token", tokenService.getAndUpdateToken(userPojo.getEmail()));
            map.put("homeworkDetails", homeworkService.getHomeworkDetails(homeworkid));
            return Result.success("已成功获取作业详细信息", map);
        }
    }

    @RequestMapping(value = "/getHomeworkAttachment", method = RequestMethod.GET)
    public String getResource(HttpServletResponse response,
                              @RequestParam("userid") int userid,
                              @RequestParam("token") String token,
                              @RequestParam("classid") int classid,
                              @RequestParam("homeworkid") int homeworkid) throws IOException {

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
                HomeworkPojo homeworkAttachment = homeworkService.getHomeworkAttachment(homeworkid);
                if (homeworkAttachment != null && homeworkAttachment.getAttachment() != null) {
                    return Result.success(homeworkAttachment.getAttachmentName(), homeworkAttachment.getAttachment(), response);
                } else {
                    return Result.defeat("无对应资源内容");
                }
            } else {
                return Result.defeat("您不在班级中");
            }
        }
    }

    @RequestMapping(value = "/deleteHomework", method = RequestMethod.DELETE)
    public String deleteHomework(@RequestParam("userid") int userid,
                                 @RequestParam("token") String token,
                                 @RequestParam("classid") int classid,
                                 @RequestParam("homeworkid") int homeworkid) {

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
                // 可以删除
                if (homeworkService.deleteHomework(homeworkid)) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("token", tokenService.getAndUpdateToken(userPojo.getEmail()));
                    return Result.success("您已成功删除作业", map);
                } else {
                    return Result.defeat("作业不存在，删除作业失败");
                }
            } else {
                return Result.defeat("您不是班级拥有者");
            }
        }
    }

}
