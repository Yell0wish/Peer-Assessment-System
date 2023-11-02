package com.ye.controller;

import com.ye.pojo.ClassPojo;
import com.ye.pojo.UserPojo;
import com.ye.service.ClassService;
import com.ye.service.HomeworkService;
import com.ye.service.TokenService;
import com.ye.service.UserService;
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

import java.lang.annotation.Retention;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
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

    @RequestMapping(value = "/addHomework", method = RequestMethod.POST)
    public String addHomework(@RequestParam("userid") int userid,
                              @RequestParam("token") String token,
                              @RequestParam("classid") int classid,
                              @RequestParam("title") String title,
                              @RequestParam("content") String content,
                              @RequestParam("attachmentName") String attachmentName,
                              @RequestParam("attachment") MultipartFile attachment,
                              @RequestParam("submitTimeString") String submitTimeString,
                              @RequestParam("correctTimeString") String correctTimeString,
                              @RequestParam("scoreMethod") String scoreMethod) {

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

                String[] parts = scoreMethod.split(";");
                if (parts.length == 2) {
                    try {
                        double a = Double.parseDouble(parts[0]);
                        double b = Double.parseDouble(parts[1]);

                        if (Math.abs(a + b - 1) > 1e-4) {
                            throw new NumberFormatException();
                        }
                    } catch (NumberFormatException e) {
                        return Result.defeat("分配方式不正确");
                    }
                } else {
                    return Result.defeat("分配方式不正确");
                }


                // 进行日期格式转换
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                sdf.setLenient(false);
                Date submitTime = null, correctTime = null;
                try {
                    submitTime = sdf.parse(submitTimeString);
                    correctTime = sdf.parse(correctTimeString);
                } catch (Exception e) {
                    return Result.defeat("日期格式不正确，应为 yyyy-MM-dd HH:mm:ss");
                }
                ZoneId zoneId = ZoneId.of("Asia/Shanghai"); // 指定"Asia/Shanghai"时区（北京时间）
                Date date = Date.from(LocalDateTime.now(zoneId).atZone(zoneId).toInstant());// 获取当前时间
                if (date.compareTo(submitTime) >= 0 || submitTime.compareTo(correctTime) >= 0) {
                    return Result.defeat("截至时间设置不正确");
                }


                homeworkService.addHomework(classid, title, content, attachmentName, attachment, submitTime, correctTime, scoreMethod);
                Map<String, Object> map = new HashMap<>();
                map.put("token", tokenService.getAndUpdateToken(userPojo.getEmail()));
                return Result.success("您已成功添加资源", map);
            } else {
                return Result.defeat("您不是班级拥有者");
            }
        }
    }
}
