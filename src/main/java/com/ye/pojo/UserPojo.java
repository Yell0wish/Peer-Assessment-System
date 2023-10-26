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
@TableName(value = "user")
public class UserPojo {
    @TableId(value = "uuid", type = IdType.AUTO)
    private int uuid;

    @TableField(value = "name")
    private String username;

    @TableField(value = "email")
    private String email;

    @TableField(value = "password")
    private String password;

    @TableField(value = "identity")
    private int identity;

    @TableField(value = "school")
    private String school;

    @TableField(value = "school_code")
    private String schoolCode;

    public UserPojo(String email, String password){
        this.email = email;
        this.password = password;
        this.uuid = 0;
        this.identity = 3;
        this.username = "用户";
    }

}
