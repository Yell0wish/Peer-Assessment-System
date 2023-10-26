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
@TableName(value = "post")
public class PostPojo {
    @TableId(value = "uuid", type = IdType.AUTO)
    private int uuid;

    @TableField(value = "classID")
    private int classID;

    @TableField(value = "userID")
    private int userID;

    @TableField(value = "title")
    private String title;

    @TableField(value = "content")
    private String content;

    @TableField(value = "time")
    private Date time;
}
