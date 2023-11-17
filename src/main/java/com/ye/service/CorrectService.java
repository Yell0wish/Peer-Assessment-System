package com.ye.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ye.dao.CorrectDao;
import com.ye.dao.SubmitDao;
import com.ye.pojo.CorrectPojo;
import com.ye.pojo.SubmitPojo;
import com.ye.utils.AllocateAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class CorrectService {
    @Autowired
    SubmitDao submitDao;

    @Autowired
    CorrectDao correctDao;

    public int allocate(int homeworkid, Double scoreMethod, int teacherid, int everyoneCorrectNum) {
        List<Integer> allSubmitID = submitDao.getAllSubmitID(homeworkid);
        if (allSubmitID.isEmpty()) {
            return 0;
        }

        if (allSubmitID.size() == 1 && scoreMethod != 1) {
            return 0;
        }

        if (allSubmitID.size() <= everyoneCorrectNum && scoreMethod != 1) {
            return -1;
        }

        if (scoreMethod != 0) {
            for (int i : allSubmitID) {
                insert(homeworkid, i, teacherid);
            }
        }

        if (allSubmitID.size() == 1 || scoreMethod == 1) {
            return 1;
        }
        Map<Integer, List<Integer>> integerListMap = AllocateAlgorithm.allocateRandom(allSubmitID, everyoneCorrectNum);
        // 每个key都批改所有value的作业
        assert integerListMap != null;
        for (Map.Entry<Integer, List<Integer>> entry : integerListMap.entrySet()) {
            for (int i : entry.getValue()) {
                insert(homeworkid, i, entry.getKey());
            }
        }
        return 1;
    }

    public void insert(int homeworkid, int userO, int userC) {
        CorrectPojo correctPojo = new CorrectPojo();
        correctPojo.setUserO(userO);
        correctPojo.setUserC(userC);
        correctPojo.setHomeworkID(homeworkid);
        correctDao.insert(correctPojo);
    }

    public boolean canCorrect(int homeworkid, int userid, int studentid) {
        return correctDao.exists(new QueryWrapper<CorrectPojo>().eq("userid_o", studentid).eq("userid_c", userid).eq("homeworkid", homeworkid));
    }

    public List<CorrectPojo> getAllocatedList(int userid) {
        return correctDao.selectList(new QueryWrapper<CorrectPojo>().eq("userid_c", userid));
        // return correctDao.getAllocatedList(userid);
    }

    public CorrectPojo selectByID(int correctid) {
        return correctDao.selectById(correctid);
    }

    public void addCorrect(int correctid, double score, String comment) {
        CorrectPojo correctPojo = correctDao.selectById(correctid);
        correctPojo.setScore(score);
        correctPojo.setComment(comment);
        ZoneId zoneId = ZoneId.of("Asia/Shanghai"); // 指定"Asia/Shanghai"时区（北京时间）
        Date date = Date.from(LocalDateTime.now(zoneId).atZone(zoneId).toInstant());// 获取当前时间
        correctPojo.setTime(date);

        correctDao.updateById(correctPojo);
    }
}
