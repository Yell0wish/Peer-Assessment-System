package com.ye.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ye.dao.SubmitDao;
import com.ye.pojo.HomeworkPojo;
import com.ye.pojo.SubmitPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.util.annotation.Nullable;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class SubmitService {
    @Autowired
    SubmitDao submitDao;

    public boolean addOrUpdateSubmit(int homeworkid, int userid, String content, String attachmentName, MultipartFile attachment, Date date) {
        byte[] file = null;

        if(!attachment.isEmpty()){ // 如果文件非空
            try {
                file = attachment.getBytes();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        List<SubmitPojo> submitPojos = submitDao.selectList(new QueryWrapper<SubmitPojo>().eq("homeworkid", homeworkid).eq("userid", userid));
        if (submitPojos.size() > 1) {
            return false;
        }
        SubmitPojo submitPojo1 = submitPojos.get(0);
        if (submitPojo1 != null) {
            submitPojo1.setContent(content);
            submitPojo1.setAttachmentName(attachmentName);
            submitPojo1.setAttachment(file);
            submitPojo1.setTime(date);
            submitDao.updateById(submitPojo1);
            return true;
        }
        SubmitPojo submitPojo = new SubmitPojo();
        submitPojo.setHomeworkID(homeworkid);
        submitPojo.setUserID(userid);
        submitPojo.setContent(content);
        submitPojo.setAttachmentName(attachmentName);
        submitPojo.setAttachment(file);
        submitDao.insert(submitPojo);
        return true;
    }

    @Nullable
    public SubmitPojo getSubmitDetails(int userid, int homeworkid) {
        List<SubmitPojo> submitPojos = submitDao.selectList(new QueryWrapper<SubmitPojo>().select("uuid", "userid", "homeworkid", "userid", "time", "content", "attachment_name").eq("userid", userid).eq("homeworkid", homeworkid));
        if (submitPojos.size() != 1) {
            return null;
        }

        return submitPojos.get(0);
    }

    public SubmitPojo getSubmitAttachment(int userid, int homeworkid) {
        List<SubmitPojo> submitPojos = submitDao.selectList(new QueryWrapper<SubmitPojo>().select("uuid","userid", "attachment", "attachment_name").eq("userid", userid).eq("homeworkid", homeworkid));
        if (submitPojos.size() != 1) {
            return null;
        }

        return submitPojos.get(0);
    }
}
