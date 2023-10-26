package com.ye.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ye.pojo.ClassInformationPublicPojo;
import com.ye.pojo.SCPojo;
import com.ye.pojo.UserInformationPublicPojo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SCDao extends BaseMapper<SCPojo> {
    @Select("select class.uuid as cuuid, class.name as cname, max_num, num, user.name as uname from sc, class, user where class.uuid = sc.classid and user.uuid = sc.userid and sc.userid = #{userid}")
    @Results({
            @Result(column = "cuuid", property = "uuid"),
            @Result(column = "cname", property = "classname"),
            @Result(column = "max_num", property = "maxNum"),
            @Result(column = "num", property = "num"),
            @Result(column = "uname", property = "ownerName"),

            // 指定其他列与字段的映射关系
    })
    List<ClassInformationPublicPojo> getAttendedCoursePublcInformation(@Param("userid") int userid);

    @Select("select user.name from user, sc where #{classid} = classid and user.uuid = userID")
    List<String> getAttendeeList(@Param("classid") int classid);
}
