package com.ye.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ye.pojo.UserInformationPublicPojo;
import com.ye.pojo.UserPojo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserDao extends BaseMapper<UserPojo> {
    @Select("select name, email, identity from user where uuid = #{userid}")
    @Results({
            @Result(column = "name", property = "username"),

            // @Result(column = "name", property = "classname"),
            // 指定其他列与字段的映射关系
    })
    UserInformationPublicPojo getPublcInformation(@Param("userid") int userid);
}
