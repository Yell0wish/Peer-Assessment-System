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
@TableName(value = "submit")
public class SubmitPojo {
    @TableId(value = "uuid", type = IdType.AUTO)
    private int uuid;

    @TableField(value = "homeworkID")
    private int homeworkID;

    @TableField(value = "userID")
    private int userID;

    @TableField(value = "time")
    private Date time;

    @TableField(value = "content")
    private String content;

    @TableField(value = "attachment_name")
    private String attachmentName;

    @TableField(value = "attachment")
    private byte[] attachment;

    @TableField(value = "score")
    private double score;

    @TableField(value = "corrected")
    private int corrected;

    @TableField(value = "integrity")
    private int integrity;
}
