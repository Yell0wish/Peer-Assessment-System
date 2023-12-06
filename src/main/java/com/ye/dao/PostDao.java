package com.ye.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ye.pojo.PostPojo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostDao extends BaseMapper<PostPojo> {
}
