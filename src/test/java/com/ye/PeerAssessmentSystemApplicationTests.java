package com.ye;

import com.ye.pojo.FilePojo;
import com.ye.service.*;
import com.ye.utils.Result;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class PeerAssessmentSystemApplicationTests {
    @Autowired
    EmailService emailService;

    @Autowired
    UserService userService;

    @Autowired
    SCService scService;

    @Autowired
    InformationService informationService;

    @Autowired
    ClassSourceService classSourceService;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    RedisService redisService;

    @Autowired
    HomeworkService homeworkService;
    @Autowired
    SubmitService submitService;
    @Test
    void contextLoads() {
        //userService.signup("丁真", "雪豹");
        //System.out.println(RandomProduct.getCheckCode());\
//        System.out.println(Validity.isEmail("3104812631@qq.com"));
        //scService.studentIsInclass(12, 10006);
//        informationService.sendInformation(1, "114514");
//        FilePojo resource = classSourceService.getResource(3);
//        System.out.println(resource.getFileName());
//        emailService.sendEmail();
        // System.out.println(valueOperations.get("jinitaimei"));
//        String a = "a";
//        System.out.println(a.equals(null));
        //System.out.println(redisService.getAndDeleteFromDatabase("21301113@bjtu.edu.cnreset"));
        //System.out.println(System.currentTimeMillis());
        //System.out.println(scService.getAttendedCoursePublcInformation(10011));
//        System.out.println(scService.getAttendeeList(12));
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date submitTime = null, correctTime = null;
//        try {
//            submitTime =  sdf.parse("yyyy-MM-dd HH:mm:ss");
//        } catch (Exception e) {
//            System.out.println("haha");
//        }
//        String input = "fdsa";
//        String[] parts = input.split(";");
//        System.out.println(Arrays.toString(parts));

        System.out.println(homeworkService.isHomeworkTeacher(10, 10007));

    }

}
