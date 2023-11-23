package com.ye.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ye.dao.SubmitDao;
import com.ye.dao.TeacherRecorrectDao;
import com.ye.pojo.SubmitPojo;
import com.ye.pojo.TeacherRecorrectPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TeacherRecorrectService {
    @Autowired
    TeacherRecorrectDao teacherRecorrectDao;
    @Autowired
    SubmitDao submitDao;

    public void insertList(List<Integer> teacherRecorrectList, int homeworkid, int teacherid) {
        Set<Integer> hashSet = new HashSet<>(teacherRecorrectList);

        for (int i : hashSet) {
            TeacherRecorrectPojo teacherRecorrectPojo = new TeacherRecorrectPojo();
            teacherRecorrectPojo.setTeacherid(teacherid);
            submitDao.selectList(new QueryWrapper<SubmitPojo>().select("uuid") .eq("userid", i).eq("homeworkid", homeworkid)).forEach(submitPojo -> {
                teacherRecorrectPojo.setSubmitid(submitPojo.getUuid());
            });
            teacherRecorrectDao.insert(teacherRecorrectPojo);
        }
    }
}
