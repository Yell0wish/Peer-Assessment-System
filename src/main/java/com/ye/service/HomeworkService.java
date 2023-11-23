package com.ye.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ye.dao.HomeworkDao;
import com.ye.pojo.HomeworkPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.util.annotation.Nullable;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class HomeworkService {
    @Autowired
    HomeworkDao homeworkDao;

    public void addHomework(int classid, String title, String content, String attachmentName, MultipartFile attachment, Date submitTime, double default_score) {
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
        homeworkPojo.setDefaultScore(default_score);
        homeworkPojo.setScoreMethod(-1);

//        homeworkPojo.setCorrectTime(correctTime);
//        homeworkPojo.setScoreMethod(scoreMethod);

        homeworkDao.insert(homeworkPojo);
    }

    public Object getHomeworkList(int classid) {
        return homeworkDao.selectList(new QueryWrapper<HomeworkPojo>().select("uuid", "classid", "title", "submit_time", "correct_time").eq("classid", classid));
    }

    public List<HomeworkPojo> getHomeworkDetails(int homeworkid) {
        return homeworkDao.selectList(new QueryWrapper<HomeworkPojo>().select("uuid", "classid", "title", "content","attachment_name", "date", "submit_time", "correct_time", "score_method").eq("uuid", homeworkid));
    }

    @Nullable
    public HomeworkPojo getHomeworkRoughly(int homeworkid) {
        return homeworkDao.selectOne(new QueryWrapper<HomeworkPojo>().select("uuid", "classid", "title", "attachment_name", "date", "submit_time", "correct_time", "score_method", "corrected").eq("uuid", homeworkid));
    }

    public HomeworkPojo getHomeworkAttachment(int homeworkid) {
        return homeworkDao.selectOne(new QueryWrapper<HomeworkPojo>().select("uuid", "attachment_name", "attachment").eq("uuid", homeworkid));
    }

    public HomeworkPojo selectByHomeworkID(int homeworkid) {
        return homeworkDao.selectOne(new QueryWrapper<HomeworkPojo>().select("uuid", "classid", "title", "content","attachment_name", "date", "submit_time", "correct_time", "score_method").eq("uuid", homeworkid));
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

    public void updateHomework(int homeworkid, Date correctTime, double scoreMethod) {
        homeworkDao.updateMyself(homeworkid, correctTime, scoreMethod);
    }

    public void setCorrected(int homeworkid, int i) {
        HomeworkPojo homeworkPojo = homeworkDao.selectList(new QueryWrapper<HomeworkPojo>().eq("uuid", homeworkid)).get(0);
        homeworkPojo.setCorrected(i);
        homeworkDao.updateById(homeworkPojo);
    }
}
