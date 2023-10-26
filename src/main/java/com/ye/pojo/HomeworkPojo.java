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
@TableName(value = "homework")
public class HomeworkPojo {
    @TableId(value = "uuid", type = IdType.AUTO)
    private int uuid;

    @TableField(value = "classID")
    private int classID;

    @TableField(value = "title")
    private String title;

    @TableField(value = "content")
    private String content;

    @TableField(value = "attachment")
    private byte[] attachment;

    @TableField(value = "time")
    private Date time;

    @TableField(value = "submit_time")
    private Date submitTime;

    @TableField(value = "correct_time")
    private Date correctTime;

    @TableField(value = "score_method")
    private String score_method;
}
