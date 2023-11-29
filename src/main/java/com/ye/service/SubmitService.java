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
import java.util.HashMap;
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

        if (submitPojos.size() == 1) {
            SubmitPojo submitPojo1 = submitPojos.get(0);
            submitPojo1.setContent(content);
            submitPojo1.setAttachmentName(attachmentName);
            submitPojo1.setAttachment(file);
            submitPojo1.setTime(date);
            submitPojo1.setScore(-1);
            submitPojo1.setCorrected(0);
            submitDao.updateById(submitPojo1);
            return true;
        }
        SubmitPojo submitPojo = new SubmitPojo();
        submitPojo.setHomeworkID(homeworkid);
        submitPojo.setUserID(userid);
        submitPojo.setContent(content);
        submitPojo.setAttachmentName(attachmentName);
        submitPojo.setAttachment(file);
        submitPojo.setScore(-1);
        submitPojo.setCorrected(0);
        submitDao.insert(submitPojo);
        return true;
    }

    @Nullable
    public SubmitPojo getSubmitDetails(int userid, int homeworkid) {
        List<SubmitPojo> submitPojos = submitDao.selectList(new QueryWrapper<SubmitPojo>().select("uuid", "userid", "homeworkid", "userid", "time", "content", "attachment_name", "score", "corrected").eq("userid", userid).eq("homeworkid", homeworkid));
        if (submitPojos.size() != 1) {
            return null;
        }

        return submitPojos.get(0);
    }

    @Nullable
    public SubmitPojo getSubmitDetails(int submitid) {
        List<SubmitPojo> submitPojos = submitDao.selectList(new QueryWrapper<SubmitPojo>().select("uuid", "userid", "homeworkid", "userid", "time", "content", "attachment_name", "score", "corrected").eq("uuid", submitid));
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

    @Nullable
    public SubmitPojo selectByID(int submitid) {
        return submitDao.selectById(submitid);
    }

    public void updateIntegrity(int homeworkid, int studentid, int i) {
        SubmitPojo submitPojo = submitDao.selectList(new QueryWrapper<SubmitPojo>().eq("homeworkid", homeworkid).eq("userid", studentid)).get(0);
        submitPojo.setIntegrity(i);
        submitDao.updateById(submitPojo);
    }

    public List<SubmitPojo> getSubmitList(int homeworkid) {
        return submitDao.selectList(new QueryWrapper<SubmitPojo>().select("uuid", "userid", "time", "content", "attachment_name", "score", "corrected", "homeworkid", "integrity").eq("homeworkid", homeworkid));
    }

    public void updateScore(int submitid, double score) {
        SubmitPojo submitPojo = getSubmitDetails(submitid);
        assert submitPojo != null;
        submitDao.updateScore(submitPojo.getHomeworkID(), submitPojo.getUserID(), score);
    }

    public HashMap<Integer, Integer> getHomeworkStatisticStage(int homeworkid) {
        // 需要90分以上 80-90 70-80 60-70 60以下
        HashMap<Integer, Integer> map = new HashMap<>();
        submitDao.selectList(new QueryWrapper<SubmitPojo>().select("score").eq("homeworkid", homeworkid)).forEach(submitPojo -> {
            if (submitPojo.getScore() >= 90) {
                map.put(5, map.getOrDefault(5, 0) + 1);
            } else if (submitPojo.getScore() >= 80) {
                map.put(4, map.getOrDefault(4, 0) + 1);
            } else if (submitPojo.getScore() >= 70) {
                map.put(3, map.getOrDefault(3, 0) + 1);
            } else if (submitPojo.getScore() >= 60) {
                map.put(2, map.getOrDefault(2, 0) + 1);
            } else if (submitPojo.getScore() >= 0) {
                map.put(1, map.getOrDefault(1, 0) + 1);
            }
        });
        return map;
    }
}
