package com.ye.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "response_post")
public class ResponsePostPojo {
    @TableId(value = "uuid", type = IdType.AUTO)
    private int uuid;

    @TableField(value = "postID")
    private int postID;

    @TableField(value = "userID")
    private int userID;

    @TableField(value = "information")
    private String information;

    @TableField(value = "time")
    private Date time;

    @TableField(value = "pic")
    private String pic;

    @TableField(value = "name")
    private String name;
}
