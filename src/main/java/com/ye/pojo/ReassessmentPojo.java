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
@TableName(value = "reassessment")
public class ReassessmentPojo {
    @TableId(value = "uuid", type = IdType.AUTO)
    private int uuid;

    @TableField(value = "homeworkID")
    private int homeworkID;

    @TableField(value = "userID")
    private int userID;

    @TableField(value = "score")
    private int score;

    @TableField(value = "time")
    private Date time;

    @TableField(value = "state")
    private int state;
}
