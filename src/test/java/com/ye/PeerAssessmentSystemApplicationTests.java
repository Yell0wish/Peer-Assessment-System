package com.ye;

import com.ye.pojo.FilePojo;
import com.ye.service.ClassSourceService;
import com.ye.service.InformationService;
import com.ye.service.SCService;
import com.ye.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PeerAssessmentSystemApplicationTests {

    @Autowired
    UserService userService;

    @Autowired
    SCService scService;

    @Autowired
    InformationService informationService;

    @Autowired
    ClassSourceService classSourceService;
    @Test
    void contextLoads() {
        //userService.signup("丁真", "雪豹");
        //System.out.println(RandomProduct.getCheckCode());\
//        System.out.println(Validity.isEmail("3104812631@qq.com"));
        //scService.studentIsInclass(12, 10006);
//        informationService.sendInformation(1, "114514");
        FilePojo resource = classSourceService.getResource(3);
        System.out.println(resource.getFileName());

    }

}
