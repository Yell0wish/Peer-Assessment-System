package com.ye.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ye.dao.ClassSourceDao;
import com.ye.pojo.ClassSourcePojo;
import com.ye.pojo.FilePojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ClassSourceService {
    @Autowired
    ClassSourceDao classSourceDao;

    public void addClassSource(int classid, String sourceName,  MultipartFile attachment) {
        byte[] file = null;

        if(!attachment.isEmpty()){ // 如果文件非空
            try {
                file = attachment.getBytes();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        ClassSourcePojo classSourcePojo = new ClassSourcePojo();
        classSourcePojo.setClassid(classid);
        classSourcePojo.setName(sourceName);
        classSourcePojo.setAttachment(file);

        classSourceDao.insert(classSourcePojo);
    }

    public FilePojo getResource(int sourceid){
        return classSourceDao.getFile(sourceid);
    }

    public List<ClassSourcePojo> getResourceList(int classid) {
        return classSourceDao.selectList(new QueryWrapper<ClassSourcePojo>().select("uuid", "classid", "name", "time").eq("classID", classid));
    }
}
