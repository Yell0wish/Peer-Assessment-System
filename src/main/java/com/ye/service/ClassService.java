package com.ye.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ye.dao.ClassDao;
import com.ye.pojo.ClassPojo;
import com.ye.pojo.ClassSourcePojo;
import com.ye.utils.RandomProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ClassService {
    @Autowired
    ClassDao classDao;


    public int createClass(int userid, String className) {
        ClassPojo classPojo = new ClassPojo();
        classPojo.setNum(0);
        classPojo.setClassname(className);
        classPojo.setMaxNum(500);
        classPojo.setUserid(userid);
        classDao.insert(classPojo);
        return classPojo.getClassid();
    }

    public ClassPojo selectClassByID(int classid) {
        return classDao.selectById(classid);
    }

    public List<ClassPojo> selectAllTeachingClasses(int userid) {
        // return classDao.selectList(new QueryWrapper<ClassPojo>().select("uuid", "max_num").eq("userID", userid));
        return classDao.selectList(new QueryWrapper<ClassPojo>().eq("userID", userid));
        // return  classDao.selectAllClasses(userid);
    }

    public void getAccessCode(ClassPojo classPojo) {
        classPojo.setAccessCode(RandomProduct.generateAccessCode(classPojo.getClassid()));
        classDao.update(classPojo, new QueryWrapper<ClassPojo>().eq("uuid", classPojo.getClassid()));
    }

    public ClassPojo selectClassByAccessCode(String accessCode){
        return classDao.selectOne(new QueryWrapper<ClassPojo>().eq("accessCode", accessCode));
    }

    public ClassPojo selectClassByClassid(int classid){
        return classDao.selectById(classid);
    }

    public void deleteTeachingClass(int classid) {
        classDao.deleteById(classid);
    }

}
