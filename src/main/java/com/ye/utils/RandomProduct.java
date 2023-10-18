package com.ye.utils;

import java.util.Random;

public class RandomProduct {
    public static String getCheckCode(){
        Random random = new Random();
        StringBuilder checkcode = new StringBuilder();
        for(int i = 0; i < 5; i++){
            checkcode.append(random.nextInt(10));
        }
        return checkcode.toString();

    }

    public static String getSalt(){
        Random random = new Random();
        StringBuilder checkcode = new StringBuilder();
        for(int i = 0; i < 8; i++){
            checkcode.append(random.nextInt(10));
        }
        return checkcode.toString();
    }

    // 生成唯一的6位包含数字和字符的码
    public static String generateAccessCode(int uuid) {


        // 随机生成的字符集合（包含数字和字符）
        String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

        // 创建一个随机数生成器
        Random random = new Random();

        // 生成唯一码
        StringBuilder uniqueCode = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            // 从字符集合中随机选择一个字符
            char randomChar = characters.charAt(random.nextInt(characters.length()));
            uniqueCode.append(randomChar);
        }

        // 返回生成的唯一码
        return uniqueCode.toString() + uuid;
    }
}
