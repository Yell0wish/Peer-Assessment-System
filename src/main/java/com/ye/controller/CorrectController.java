package com.ye.controller;

import com.ye.pojo.*;
import com.ye.service.*;
import com.ye.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class CorrectController {
    @Autowired
    UserService userService;
    @Autowired
    TokenService tokenService;
    @Autowired
    ClassService classService;
    @Autowired
    HomeworkService homeworkService;
    @Autowired
    CorrectService correctService;
    @Autowired
    SubmitService submitService;

    @RequestMapping(value = "/allocateHomework", method = RequestMethod.POST)
    public String attendCourse(@RequestParam("userid") int userid,
                               @RequestParam("token") String token,
                               @RequestParam("homeworkid") int homeworkid,
                               @RequestParam("correctTimeString") String correctTimeString,
                               @RequestParam("scoreMethod") String scoreMethod) {

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

            ClassPojo classPojo = classService.selectClassByID(homeworkPojo.getClassID());
            if (classPojo == null) {
                return Result.defeat("班级不存在");
            }
            if (classPojo.getUserid() == userid) {
                // todo 正确进行处理
                if (homeworkPojo.getCorrectTime() != null) {
                    return Result.defeat("已经分配过了，无法再分配");
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
                Date submitTime = homeworkPojo.getSubmitTime(), correctTime = null;
                try {
                    correctTime = sdf.parse(correctTimeString);
                } catch (Exception e) {
                    return Result.defeat("日期格式不正确，应为 yyyy-MM-dd HH:mm:ss");
                }
                ZoneId zoneId = ZoneId.of("Asia/Shanghai"); // 指定"Asia/Shanghai"时区（北京时间）
                Date date = Date.from(LocalDateTime.now(zoneId).atZone(zoneId).toInstant());// 获取当前时间
                if (date.compareTo(submitTime) <= 0) {
                    return Result.defeat("还没到提交截至日期，不能进行批改");
                }
                if (date.compareTo(correctTime) >= 0 || submitTime.compareTo(correctTime) >= 0) {
                    return Result.defeat("截至时间设置不正确");
                }
                homeworkService.updateHomework(homeworkid, correctTime, scoreMethod);
                boolean allocate = correctService.allocate(homeworkid, Double.parseDouble(parts[0]), classPojo.getUserid());
                if (!allocate) {
                    return Result.defeat("作业提交人数小于等于1，无法学生互评");
                }
                Map<String, Object> map = new HashMap<>();
                map.put("token", tokenService.getAndUpdateToken(userPojo.getEmail()));
                return Result.success("您已成功分配作业", map);

            } else {
                return Result.defeat("您不是班级拥有者");
            }
        }
    }

    @RequestMapping(value = "/getAllocatedList", method = RequestMethod.GET)
    public String getAllocatedList(@RequestParam("userid") int userid,
                                   @RequestParam("token") String token) {

        UserPojo userPojo = userService.selectByID(userid);
        if (userPojo == null) {
            return Result.defeat("用户ID不存在");
        } else if (!token.equals(tokenService.getToken(userPojo.getEmail()))) {
            return Result.defeat("token不正确");
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("token", tokenService.getAndUpdateToken(userPojo.getEmail()));
            map.put("allocatedList", correctService.getAllocatedList(userid));
            return Result.success("已成功获取分配作业列表", map);
        }
    }

    @RequestMapping(value = "/correctHomework", method = RequestMethod.POST)
    public String correctHomework(@RequestParam("userid") int userid,
                                  @RequestParam("token") String token,
                                  @RequestParam("correctid") int correctid,
                                  @RequestParam("score") double score,
                                  @RequestParam("comment") String comment) {

        UserPojo userPojo = userService.selectByID(userid);
        if (userPojo == null) {
            return Result.defeat("用户ID不存在");
        } else if (!token.equals(tokenService.getToken(userPojo.getEmail()))) {
            return Result.defeat("token不正确");
        } else {
            CorrectPojo correctPojo = correctService.selectByID(correctid);

            if (correctPojo == null) {
                return Result.defeat("批改分配不存在");
            }

            if (correctPojo.getUserC() != userid) {
                return Result.defeat("您无权批改学生作业");
            }

            if (correctPojo.getTime() != null) {
                return Result.defeat("您已经批改过作业");
            }

            // todo 批改时间限制
            HomeworkPojo homeworkRoughly = homeworkService.getHomeworkRoughly(correctPojo.getHomeworkID());
            if (homeworkRoughly == null) {
                return Result.defeat("作业不存在");
            }

            ZoneId zoneId = ZoneId.of("Asia/Shanghai"); // 指定"Asia/Shanghai"时区（北京时间）
            Date date = Date.from(LocalDateTime.now(zoneId).atZone(zoneId).toInstant());// 获取当前时间
            if (homeworkRoughly.getCorrectTime().compareTo(date) < 0 ) {
                ClassPojo classPojo = classService.selectClassByID(homeworkRoughly.getClassID());
                assert classPojo != null;
                if (classPojo.getUserid() != userid) { // 老师有特权，可以在批改时间过后批改
                    return Result.defeat("已经过了批改时间，无法批改");
                }

            }

            correctService.addCorrect(correctid, score, comment);

            Map<String, Object> map = new HashMap<>();
            map.put("token", tokenService.getAndUpdateToken(userPojo.getEmail()));
            return Result.success("已成功批改作业", map);
        }
    }
}
