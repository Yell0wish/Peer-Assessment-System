package com.ye.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "class")
public class ClassPojo {
    @TableId(value = "uuid", type = IdType.AUTO)
    private int classid;

    @TableField(value = "name")
    private String classname;

    @TableField(value = "max_num")
    private int maxNum;

    @TableField(value = "userID")
    private int userid;

    @TableField(value = "num")
    private int num;

    @TableField(value = "accessCode")
    private String accessCode;
}
