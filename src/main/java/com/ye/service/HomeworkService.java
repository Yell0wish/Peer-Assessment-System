package com.ye.service;

import com.ye.dao.HomeworkDao;
import com.ye.pojo.HomeworkPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

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
        homeworkPojo.setScore_method(scoreMethod);

        homeworkDao.insert(homeworkPojo);
    }
}
