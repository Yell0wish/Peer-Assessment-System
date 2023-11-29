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
@TableName(value = "correct")
public class CorrectExtendPojo {
    @TableId(value = "uuid", type = IdType.AUTO)
    private int uuid;

    @TableField(value = "homeworkID")
    private int homeworkID;

    @TableField(value = "userID_o")
    private int userO;

    @TableField(value = "userID_c")
    private int userC;

    @TableField(value = "score")
    private double score;

    @TableField(value = "time")
    private Date time;

    @TableField(value = "comment")
    private String comment;

    @TableField(value = "classID", exist = false)
    private int classID;

    @TableField(value = "title", exist = false)
    private String title;

    @TableField(value = "correct_time", exist = false)
    private String correctTime;
}
