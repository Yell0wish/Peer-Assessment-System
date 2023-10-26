package com.ye.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ye.dao.SCDao;
import com.ye.pojo.ClassInformationPublicPojo;
import com.ye.pojo.SCPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SCService {
    @Autowired
    SCDao scDao;

    public boolean studentIsInclass(int classid, int studentid){
        List<SCPojo> scPojos = scDao.selectList(new QueryWrapper<SCPojo>().eq("userID", studentid).eq("classID", classid));
        // System.out.println(scPojos);
        return !scPojos.isEmpty(); //元素空 返回 false 非空 返回true

    }

    public void attendClass(int classid, int studentid){
        SCPojo scPojo = new SCPojo();
        scPojo.setClassid(classid);
        scPojo.setUserid(studentid);
        scDao.insert(scPojo);

    }

    public void quitCourse(int classid, int studentid){
        scDao.delete(new QueryWrapper<SCPojo>().eq("userID", studentid).eq("classID", classid));
    }

    public List<ClassInformationPublicPojo> getAttendedCoursePublcInformation(int userid){
        return scDao.getAttendedCoursePublcInformation(userid);
    }

    public List<String> getAttendeeList(int classid) {
        return scDao.getAttendeeList(classid);
    }

}
