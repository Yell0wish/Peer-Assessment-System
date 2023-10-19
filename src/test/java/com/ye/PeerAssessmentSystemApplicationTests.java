package com.ye;

import com.ye.pojo.FilePojo;
import com.ye.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

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
    }

}
