package com.ye.controller;

import com.ye.pojo.ClassPojo;
import com.ye.pojo.HomeworkPojo;
import com.ye.pojo.SubmitPojo;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class SubmitController {
    @Autowired
    UserService userService;

    @Autowired
    TokenService tokenService;

    @Autowired
    ClassService classService;

    @Autowired
    SubmitService submitService;

    @Autowired
    HomeworkService homeworkService;

    @Autowired
    SCService scService;

    @RequestMapping(value = "/addSubmit", method = RequestMethod.POST)
    public String addSubmit(@RequestParam("userid") int userid,
                            @RequestParam("token") String token,
                            @RequestParam("homeworkid") int homeworkid,
                            @RequestParam("content") String content,
                            @RequestParam("attachmentName") String attachmentName,
                            @RequestParam("attachment") MultipartFile attachment) {

        UserPojo userPojo = userService.selectByID(userid);
        if (userPojo == null) {
            return Result.defeat("用户ID不存在");
        } else if (!token.equals(tokenService.getToken(userPojo.getEmail()))) {
            return Result.defeat("token不正确");
        } else {
            HomeworkPojo homeworkPojo = homeworkService.selectClassByHomeworkID(homeworkid);
            if (homeworkPojo == null) {
                return Result.defeat("作业不存在");
            }
            int classid = homeworkPojo.getClassID();
            ClassPojo classPojo = classService.selectClassByID(classid);
            if (classPojo == null) {
                return Result.defeat("课程不存在");
            } else if (scService.studentIsInclass(classid, userid)) { //只有班级学生才可以交作业
                ZoneId zoneId = ZoneId.of("Asia/Shanghai"); // 指定"Asia/Shanghai"时区（北京时间）
                Date submitTime = homeworkPojo.getSubmitTime();
                Date date = Date.from(LocalDateTime.now(zoneId).atZone(zoneId).toInstant());// 获取当前时间
                if (date.compareTo(submitTime) >= 0) {
                    return Result.defeat("已经过了截止时间，无法提交");
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

                boolean b = submitService.addOrUpdateSubmit(homeworkid, userid, content, attachmentName, attachment, date);
                if (!b) {
                    return Result.defeat("出现异常，未成功提交");
                }
                Map<String, Object> map = new HashMap<>();
                map.put("token", tokenService.getAndUpdateToken(userPojo.getEmail()));
                return Result.success("您已成功提交作业", map);
            } else {
                return Result.defeat("您不是班级学生");
            }
        }
    }

    @RequestMapping(value = "/getSubmitDetails", method = RequestMethod.GET)
    public String getSubmitDetails(@RequestParam("userid") int userid,
                                   @RequestParam("studentid") int studentid,
                                   @RequestParam("token") String token,
                                   @RequestParam("homeworkid") int homeworkid) {

        UserPojo userPojo = userService.selectByID(userid);
        if (userPojo == null) {
            return Result.defeat("用户ID不存在");
        } else if (!token.equals(tokenService.getToken(userPojo.getEmail()))) {
            return Result.defeat("token不正确");
        } else {
            if (userPojo.getUuid() != userid) {
                return Result.defeat("不是本人操作");
            }
            SubmitPojo submitDetails = submitService.getSubmitDetails(studentid, homeworkid);
            if (submitDetails == null) {
                return Result.defeat("未查询到相应作业");
            }
            // todo 判断是不是老师
            if (!homeworkService.isHomeworkTeacher(homeworkid, userid) && submitDetails.getUserID() != userid) {
                return Result.defeat("您无权查看学生作业");
            }

            Map<String, Object> map = new HashMap<>();
            map.put("token", tokenService.getAndUpdateToken(userPojo.getEmail()));
            map.put("submitDetails", submitDetails);
            return Result.success("已成功获取作业详细信息", map);
        }
    }

    // todo 让老师和授权的学生都可以访问
    @RequestMapping(value = "/getSubmitAttachment", method = RequestMethod.GET)
    public String getSubmitAttachment(HttpServletResponse response,
                                      @RequestParam("userid") int userid,
                                      @RequestParam("studentid") int studentid,
                                      @RequestParam("token") String token,
                                      @RequestParam("homeworkid") int homeworkid) throws IOException {

        UserPojo userPojo = userService.selectByID(userid);
        if (userPojo == null) {
            return Result.defeat("用户ID不存在");
        } else if (!token.equals(tokenService.getToken(userPojo.getEmail()))) {
            return Result.defeat("token不正确");
        } else {
            if (userPojo.getUuid() != userid) {
                return Result.defeat("不是本人操作");
            }

            SubmitPojo submitDetails = submitService.getSubmitAttachment(studentid, homeworkid);
            if (submitDetails == null) {
                return Result.defeat("未查询到相应作业");
            }
            // todo 判断是不是老师
            if (!homeworkService.isHomeworkTeacher(homeworkid, userid) && submitDetails.getUserID() != userid) {
                return Result.defeat("您无权查看学生作业");
            }
            if (submitDetails.getAttachment() == null) {
                return Result.defeat("该作业没有附件");
            }
            return Result.success(submitDetails.getAttachmentName(), submitDetails.getAttachment(), response);
        }
    }
}
