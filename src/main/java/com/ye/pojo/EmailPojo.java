package com.ye.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailPojo {
    //    邮件接收方
    private String[] receiver;
    //    邮件主题
    private String subject;
    //    邮件内容
    private String content;
}
