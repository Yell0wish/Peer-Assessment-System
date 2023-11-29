package com.ye.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ye.dao.CorrectDao;
import com.ye.dao.SubmitDao;
import com.ye.dao.TeacherRecorrectDao;
import com.ye.pojo.CorrectExtendPojo;
import com.ye.pojo.CorrectPojo;
import com.ye.pojo.HomeworkPojo;
import com.ye.pojo.SubmitPojo;
import com.ye.utils.AllocateAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static java.lang.Math.abs;

@Service
public class CorrectService {
    @Autowired
    SubmitDao submitDao;

    @Autowired
    CorrectDao correctDao;

    @Autowired
    SubmitService submitService;

    @Autowired
    TeacherRecorrectService teacherRecorrectService;

    @Autowired
    HomeworkService homeworkService;

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
        correctPojo.setScore(-1);
        correctDao.insert(correctPojo);
    }

    public boolean canCorrect(int homeworkid, int userid, int studentid) {
        return correctDao.exists(new QueryWrapper<CorrectPojo>().eq("userid_o", studentid).eq("userid_c", userid).eq("homeworkid", homeworkid));
    }

    public List<CorrectExtendPojo> getAllocatedList(int userid) {
        // return correctDao.selectList(new QueryWrapper<CorrectPojo>().eq("userid_c", userid));
        return correctDao.getAllocatedList(userid);
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

    public List<CorrectPojo> getHomeworkAllocatedList(int homeworkid) {
        return correctDao.selectList(new QueryWrapper<CorrectPojo>().eq("homeworkid", homeworkid));
    }

    public boolean getFinalScore(int homeworkid, int teacherid, double weight, double default_score) {
        // 每份作业只会执行一次本代码
        HomeworkPojo homeworkRoughly = homeworkService.getHomeworkRoughly(homeworkid);
        if (homeworkRoughly == null) {
            return false;
        }
        if (homeworkRoughly.getCorrected() == 1) {
            return false;
        }

        if (weight < 0 || weight > 1) {
            // 输入不合法
            return false;
        }

        homeworkService.setCorrected(homeworkid, 1);
        // todo 明天重构一下代码 其实不用写的这么复杂的
        // 重构完成！

        List<Integer> studentidList = submitDao.getAllSubmitID(homeworkid); // 所有提交了作业的同学的id
        if (studentidList.isEmpty()) {
            // 没人提交作业 不用批改
            return true;
        }
        double teacherScore = -1;
        double studentScore = -1;
        double finalScore;

        // 老师重新批改的list
        List<Integer> teacherRecorrectList = new ArrayList<>();

        for (int studentid : studentidList) {
            if (abs(weight - 0) > 1e-5) { // 如果不是全同学批改 那老师就需要批改
                teacherScore = correctDao.selectList(new QueryWrapper<CorrectPojo>().select("score").eq("homeworkid", homeworkid).eq("userid_c", teacherid).eq("userid_o", studentid)).get(0).getScore();
                if (abs(teacherScore + 1) <= 1e-5) { // 未批改
                    teacherScore = default_score;
                }
            }
            if (abs(weight - 1) > 1e-5) { // 如果不是全老师修改
                // 出现过同学未批改的情况 只计算已批改的同学
                // 该学生被同学批改的所有的分数
                List<CorrectPojo> studentScores = correctDao.getStudentScores(homeworkid, studentid, teacherid);
                double sum = 0;
                int count = 0;
                for (CorrectPojo correctPojo : studentScores) {
                    if (abs(correctPojo.getScore() + 1) > 1e-5) { // 已批改
                        sum += correctPojo.getScore();
                        count++;
                    } else {
                        // 设置该同学为不诚信
                        submitService.updateIntegrity(homeworkid, correctPojo.getUserC(), 0);
                    }

                }
                if (count > 0) {
                    // 给有人给同学打分时的分数
                    studentScore = sum / count;
                } else {
                    // 所有同学都没有批改且完全由学生批改 交给老师批改
                    if (abs(weight - 0) <= 1e-5) {
                        teacherRecorrectList.add(studentid);
                        studentScore = -114514;
                    }
                }
            }
            // 加权计算学生的分数
            if ((abs(studentScore + 114514) > 1e-5)) { // studentScore != -114514 即学生并不是需要老师重新批改

                if ((abs(studentScore + 1) <= 1e-5)) { // studentScore != -1
                    // 学生未批改 且老师有批改
                    finalScore = teacherScore;
                } else { // 学生已批改
                    finalScore = teacherScore * weight + studentScore * (1 - weight);
                }
                // finalScore保留一位小数？
                finalScore = (double) Math.round(finalScore * 100) / 100.0;
                System.out.println("学生" + studentid + "的分数为" + finalScore + "，其中老师给了" + teacherScore + "分，同学给了" + studentScore + "分");
                submitDao.updateScore(homeworkid, studentid, finalScore);
            }

        }
        if (!teacherRecorrectList.isEmpty()) {
            // todo 给老师发信息
            teacherRecorrectService.insertList(teacherRecorrectList, homeworkid, teacherid);
        }

        // 循环遍历所有学生 判断是否不诚信
        for (int studentid : studentidList) {
            if (abs(submitDao.selectList(new QueryWrapper<SubmitPojo>().select("integrity").eq("homeworkid", homeworkid).eq("userid", studentid)).get(0).getIntegrity() - 0) <= 1e-5) {
                // 学生不诚信
                // 该同学已经有成绩，成绩变为之前的一半
                if (abs(submitDao.selectList(new QueryWrapper<SubmitPojo>().select("score").eq("homeworkid", homeworkid).eq("userid", studentid)).get(0).getScore() + 1) > 1e-5) {
                    // 在同学未批改的情况下，不诚信的同学分数不变
                    submitDao.updateScore(homeworkid, studentid, (double) (submitDao.selectList(new QueryWrapper<SubmitPojo>().select("score").eq("homeworkid", homeworkid).eq("userid", studentid)).get(0).getScore() / 2));
                }
            }
            System.out.println("学生" + studentid + "的最终分数为" + submitDao.selectList(new QueryWrapper<SubmitPojo>().select("score").eq("homeworkid", homeworkid).eq("userid", studentid)).get(0).getScore());
        }
        return true;
    }
}
