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
@TableName(value = "information")
public class InformationPojo {
    @TableId(value = "uuid", type = IdType.AUTO)
    private int informationid;

    @TableField(value = "userid")
    private int userid;

    @TableField(value = "content")
    private String content;

    @TableField(value = "state")
    private int state;

}
