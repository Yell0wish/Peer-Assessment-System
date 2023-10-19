package com.ye.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import reactor.util.annotation.Nullable;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {
    @Autowired
    RedisTemplate redisTemplate;


    /**
     * 时间单位为秒
     * @param key
     * @param value
     * @param time
     */
    public void saveIntoDatabase(Object key, Object value, int time){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value, time, TimeUnit.SECONDS);
    }


    @Nullable
    public Object getFromDatabase(Object key){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    public void deleteFromDatabase(Object key){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.getOperations().delete(key);
    }


    @Nullable
    public Object getAndDeleteFromDatabase(Object key){
        Object fromDatabase = getFromDatabase(key);
        deleteFromDatabase(key);
        return fromDatabase;
    }

}
