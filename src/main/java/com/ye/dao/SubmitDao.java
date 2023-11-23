package com.ye.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ye.pojo.SubmitPojo;
import com.ye.pojo.UserInformationPublicPojo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SubmitDao extends BaseMapper<SubmitPojo> {
    @Select("select userid from submit where homeworkid = #{homeworkid}")
    List<Integer> getAllSubmitID(@Param("homeworkid") int homeworkid);


    @Update("update submit set score = #{score}, corrected = 1 where homeworkid = #{homeworkid} and userid = #{studentid}")
    void updateScore(int homeworkid, int studentid, Double score);
}
