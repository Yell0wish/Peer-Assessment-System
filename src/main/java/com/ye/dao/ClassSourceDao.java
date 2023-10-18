package com.ye.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ye.pojo.ClassSourcePojo;
import com.ye.pojo.FilePojo;
import com.ye.pojo.UserInformationPublicPojo;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ClassSourceDao extends BaseMapper<ClassSourcePojo> {
    @Select("select attachment, name from class_source where uuid = #{sourceid}")
    @Results({
            @Result(column = "attachment", property = "file"),

            @Result(column = "name", property = "fileName"),
            // 指定其他列与字段的映射关系
    })
    FilePojo getFile(@Param("sourceid") int sourceid);
}
