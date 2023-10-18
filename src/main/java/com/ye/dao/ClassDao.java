package com.ye.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ye.pojo.ClassPojo;
import com.ye.pojo.UserInformationPublicPojo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ClassDao extends BaseMapper<ClassPojo> {
//    @Select("select uuid from class where userID = #{userid}")
//    @Results({
//            @Result(column = "uuid", property = "classid"),
//            // @Result(column = "name", property = "classname"),
//            // 指定其他列与字段的映射关系
//    })
//    List<UserInformationPublicPojo> selectAllClasses(@Param("userid") int userid);

}
