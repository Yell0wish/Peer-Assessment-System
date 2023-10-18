package com.ye.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ye.pojo.InformationPojo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InformationDao extends BaseMapper<InformationPojo> {
}
