package com.ye.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validity {
    public static boolean isEmail(String email){
        // 定义邮箱格式的正则表达式
        String regex = "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+)\\.[A-Za-z]{2,4}$";

        // 编译正则表达式
        Pattern pattern = Pattern.compile(regex);

        // 使用正则表达式匹配字符串
        Matcher matcher = pattern.matcher(email);

        // 返回匹配结果
        return matcher.matches();
    }
}
