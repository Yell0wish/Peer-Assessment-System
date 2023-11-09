package com.ye.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ye.dao.HomeworkDao;
import com.ye.pojo.ClassPojo;
import com.ye.pojo.HomeworkPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.cert.TrustAnchor;
import java.util.Date;
import java.util.TreeMap;

@Service
public class HomeworkService {
    @Autowired
    HomeworkDao homeworkDao;

    public void addHomework(int classid, String title, String content, String attachmentName, MultipartFile attachment, Date submitTime, Date correctTime, String scoreMethod) {
        byte[] file = null;

        if(!attachment.isEmpty()){ // 如果文件非空
            try {
                file = attachment.getBytes();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        HomeworkPojo homeworkPojo = new HomeworkPojo();

        homeworkPojo.setClassID(classid);
        homeworkPojo.setTitle(title);
        homeworkPojo.setContent(content);
        homeworkPojo.setAttachmentName(attachmentName);
        homeworkPojo.setAttachment(file);
        homeworkPojo.setSubmitTime(submitTime);
        homeworkPojo.setCorrectTime(correctTime);
        homeworkPojo.setScoreMethod(scoreMethod);

        homeworkDao.insert(homeworkPojo);
    }

    public Object getHomeworkList(int classid) {
        return homeworkDao.selectList(new QueryWrapper<HomeworkPojo>().select("uuid", "classid", "title", "submit_time", "correct_time").eq("classid", classid));
    }

    public Object getHomeworkDetails(int homeworkid) {
        return homeworkDao.selectList(new QueryWrapper<HomeworkPojo>().select("uuid", "classid", "title", "content","attachment_name", "date", "submit_time", "correct_time", "score_method").eq("uuid", homeworkid));
    }

    public HomeworkPojo getHomeworkAttachment(int homeworkid) {
        return homeworkDao.selectOne(new QueryWrapper<HomeworkPojo>().select("uuid", "attachment_name", "attachment").eq("uuid", homeworkid));
    }

    public HomeworkPojo selectClassByHomeworkID(int homeworkid) {
        return homeworkDao.selectOne(new QueryWrapper<HomeworkPojo>().select("classid", "submit_time").eq("uuid", homeworkid));
    }

    public boolean deleteHomework(int homeworkid) {
        if (! homeworkDao.exists(new QueryWrapper<HomeworkPojo>().eq("uuid", homeworkid))) {
            return false;
        }
        homeworkDao.deleteById(homeworkid);
        return true;
    }

    public boolean isHomeworkTeacher(int homeworkid, int userid) {
        int homeworkMatchTeacherCount = homeworkDao.getHomeworkMatchTeacherCount(homeworkid, userid);
        return homeworkMatchTeacherCount > 0;
    }
}
