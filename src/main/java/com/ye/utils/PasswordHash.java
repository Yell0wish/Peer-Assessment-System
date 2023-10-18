package com.ye.utils;

import org.springframework.util.DigestUtils;

public class PasswordHash {
    private static PasswordHash instance;

    // 随便乱打的随机的salt值
    private final String salt = "FDHSUI32781HUIDWSA!~";


    public String getMD5(String password){
        return DigestUtils.md5DigestAsHex((password + PasswordHash.getInstance().salt).getBytes());
    }

    private PasswordHash(){
    }

    public static PasswordHash getInstance() {
        if (instance == null) {
            instance = new PasswordHash();
        }
        return instance;
    }
}
