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

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ReassessmentController {

    @Autowired
    UserService userService;

    @Autowired
    TokenService tokenService;

    @Autowired
    SubmitService submitService;

    @Autowired
    ReassessmentService reassessmentService;

    @Autowired
    HomeworkService homeworkService;

    @Autowired
    ClassService classService;

    @RequestMapping(value = "/postReassessment", method = RequestMethod.POST)
    public String postReassessment(@RequestParam("userid") int userid,
                                   @RequestParam("token") String token,
                                   @RequestParam("submitid") int submitid) {

        UserPojo userPojo = userService.selectByID(userid);
        if (userPojo == null) {
            return Result.defeat("用户ID不存在");
        } else if (!token.equals(tokenService.getToken(userPojo.getEmail()))) {
            return Result.defeat("token不正确");
        }

        SubmitPojo submitDetails = submitService.getSubmitDetails(submitid);
        if (submitDetails == null) {
            return Result.defeat("作业提交不存在");
        } else if (submitDetails.getUserID() != userid) {
            return Result.defeat("您不是作业提交者");
        } else if (reassessmentService.haveReassessmentBySubmitid(submitid)) {
            return Result.defeat("您已经申请过了");
        } else {
            if (submitDetails.getScore() == -1) {
                return Result.defeat("您的作业还未批改");
            }
            int homeworkid = submitDetails.getHomeworkID();
            HomeworkPojo homeworkRoughly = homeworkService.getHomeworkRoughly(homeworkid);
            assert homeworkRoughly != null;
            int classID = homeworkRoughly.getClassID();
            ClassPojo classDetails = classService.selectClassByID(classID);
            assert classDetails != null;
            reassessmentService.postReassessment(submitid, classDetails.getUserid());
            Map<String, Object> map = new HashMap<>();
            map.put("token", tokenService.getAndUpdateToken(userPojo.getEmail()));
            return Result.success("您已成功申请重评", map);
        }
    }

    @RequestMapping(value = "/reassessSubmit", method = RequestMethod.POST)
    public String reassessSubmit(@RequestParam("userid") int userid,
                                 @RequestParam("token") String token,
                                 @RequestParam("reassessmentid") int reassessmentid,
                                 @RequestParam("score") double score) {

        UserPojo userPojo = userService.selectByID(userid);
        if (userPojo == null) {
            return Result.defeat("用户ID不存在");
        } else if (!token.equals(tokenService.getToken(userPojo.getEmail()))) {
            return Result.defeat("token不正确");
        } else {
            if (userPojo.getUuid() != userid) {
                return Result.defeat("不是本人操作");
            }

            if (!reassessmentService.haveReassessment(reassessmentid)) {
                return Result.defeat("重评申请不存在");
            }
            int homeworkid = reassessmentService.getHomeworkid(reassessmentid);
//            // todo 判断是不是老师或已经授权
            if (!homeworkService.isHomeworkTeacher(homeworkid, userid)) {
                return Result.defeat("您无权查看学生作业");
            }
            if (score < 0 || score > 100) {
                return Result.defeat("分数不合法");
            }
            reassessmentService.reassessSubmit(reassessmentid, score);
            Map<String, Object> map = new HashMap<>();
            map.put("token", tokenService.getAndUpdateToken(userPojo.getEmail()));
            return Result.success("您已成功重评", map);
        }
    }



    @RequestMapping(value = "/getReassessmentList", method = RequestMethod.GET)
    public String getReassessmentList(@RequestParam("userid") int userid,
                                      @RequestParam("token") String token) {
        //获取用户需要重新评分的作业列表

        UserPojo userPojo = userService.selectByID(userid);
        if (userPojo == null) {
            return Result.defeat("用户ID不存在");
        } else if (!token.equals(tokenService.getToken(userPojo.getEmail()))) {
            return Result.defeat("token不正确");
        } else {
            if (userPojo.getUuid() != userid) {
                return Result.defeat("不是本人操作");
            }
            Map<String, Object> map = new HashMap<>();
            map.put("token", tokenService.getAndUpdateToken(userPojo.getEmail()));
            map.put("reassessmentList", reassessmentService.getReassessmentList(userid));
            return Result.success("已成功获取待重评作业列表", map);
        }
    }
}
