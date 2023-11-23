package com.ye.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ye.pojo.FilePojo;
import com.ye.pojo.HomeworkPojo;
import com.ye.pojo.InformationPojo;
import org.apache.ibatis.annotations.*;

import java.util.Date;

@Mapper
public interface HomeworkDao extends BaseMapper<HomeworkPojo> {
    @Select("select count(*) from homework, class where homework.classid = class.uuid and homework.uuid = #{homeworkid} and class.userid = #{userid}")
    int getHomeworkMatchTeacherCount(@Param("homeworkid") int homeworkid, @Param("userid") int userid);

    @Update("update homework set correct_time = #{correctTime}, score_method = #{scoreMethod} where uuid = #{homeworkid}")
    void updateMyself(@Param("homeworkid") int homeworkid, @Param("correctTime") Date correctTime, @Param("scoreMethod") double scoreMethod);
}
