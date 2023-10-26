package com.ye.service;

import com.ye.utils.PasswordHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.util.annotation.Nullable;

@Service
public class TokenService {
    @Autowired
    RedisService redisService;

    public String generateToken(String email){
        return PasswordHash.getInstance().getMD5(email + System.currentTimeMillis());
    }

    public void storeToken(String email, String token){
        redisService.saveIntoDatabase(email + "token", token, 30 * 60);
    }

    @Nullable
    public String getToken(String email){
        return (String) redisService.getFromDatabase(email + "token");
    }

    @Nullable
    public String getAndUpdateToken(String email){
        Number resttime = redisService.getTTL(email + "token");
        if (resttime.longValue() > 0 && resttime.longValue() <= 10 * 60) { // 重新换一个token
            String newToken = generateToken(email);
            storeToken(email, newToken);
            return newToken;
        } else {
            return getToken(email);
        }
    }

    public String firstGetToken(String email){
        String token = generateToken(email);
        storeToken(email, token);
        return token;
    }
}
