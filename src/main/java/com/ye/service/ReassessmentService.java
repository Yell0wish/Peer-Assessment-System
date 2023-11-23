package com.ye.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ye.dao.ReassessmentDao;
import com.ye.dao.SubmitDao;
import com.ye.pojo.ReassessmentPojo;
import com.ye.pojo.SubmitPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReassessmentService {
    @Autowired
    ReassessmentDao reassessmentDao;

    @Autowired
    SubmitService submitService;

    @Autowired
    SubmitDao submitDao;
    public boolean haveReassessment(int ressessmentid) {
        return !reassessmentDao.selectList(new QueryWrapper<ReassessmentPojo>().eq("uuid", ressessmentid)).isEmpty();

    }

    public boolean haveReassessmentBySubmitid(int submitid) {
        return !reassessmentDao.selectList(new QueryWrapper<ReassessmentPojo>().eq("submitid", submitid)).isEmpty();

    }

    public void postReassessment(int submitid, int userid) {
        ReassessmentPojo reassessmentPojo = new ReassessmentPojo();
        reassessmentPojo.setSubmitID(submitid);
        reassessmentPojo.setTeacherID(userid);
        reassessmentPojo.setState(0);
        reassessmentPojo.setScore(-1);
        reassessmentDao.insert(reassessmentPojo);
//        reassessmentPojo
//        reassessmentPojo.set
    }

    public int getHomeworkid(int reassessmentid) {
        int submitID = reassessmentDao.selectById(reassessmentid).getSubmitID();
        SubmitPojo submitDetails = submitService.getSubmitDetails(submitID);
        assert submitDetails != null;
        return submitDetails.getHomeworkID();
    }

    public void reassessSubmit(int reassessmentid, double score) {
        ReassessmentPojo reassessmentPojo = reassessmentDao.selectById(reassessmentid);
        reassessmentPojo.setScore(score);
        reassessmentPojo.setState(1);
        reassessmentDao.updateById(reassessmentPojo);
        // submit中的成绩也同步修改
        SubmitPojo submitDetails = submitService.getSubmitDetails(reassessmentPojo.getSubmitID());
        assert submitDetails != null;
        submitService.updateScore(submitDetails.getUuid(), score);

    }

    public List<ReassessmentPojo> getReassessmentList(int teacherid) {
        return reassessmentDao.selectList(new QueryWrapper<ReassessmentPojo>().eq("teacherid", teacherid));
    }
}
