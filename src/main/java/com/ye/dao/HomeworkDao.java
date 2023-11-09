package com.ye.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ye.pojo.FilePojo;
import com.ye.pojo.HomeworkPojo;
import com.ye.pojo.InformationPojo;
import org.apache.ibatis.annotations.*;

@Mapper
public interface HomeworkDao extends BaseMapper<HomeworkPojo> {
    @Select("select count(*) from homework, class where homework.classid = class.uuid and homework.uuid = #{homeworkid} and class.userid = #{userid}")
    int getHomeworkMatchTeacherCount(@Param("homeworkid") int homeworkid, @Param("userid") int userid);
}
