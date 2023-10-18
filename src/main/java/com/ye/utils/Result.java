package com.ye.utils;

import com.alibaba.fastjson2.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Result {
    public static String defeat(String message){
        Map<String, Object> map = new HashMap<>();
        map.put("code", 400);
        map.put("message", message);
        return JSONObject.toJSONString(map);
    }

    public static String defeat(int responseCode, String message){
        Map<String, Object> map = new HashMap<>();
        map.put("code", responseCode);
        map.put("message", message);
        return JSONObject.toJSONString(map);
    }

    public static String success(String message){
        Map<String, Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("message", message);
        return JSONObject.toJSONString(map);
    }

    public static String success(String message, Map<String, Object> data){
        Map<String, Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("message", message);
        map.put("data", data);
        return JSONObject.toJSONString(map);
    }
}
