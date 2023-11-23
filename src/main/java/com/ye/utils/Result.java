package com.ye.utils;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
        return JSONObject.toJSONString(map, JSONWriter.Feature.WriteMapNullValue);
    }

    public static String success(String fileName, byte[] file, HttpServletResponse response) throws IOException {
        JSONObject result = new JSONObject();
        response.reset();
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setContentLength((int)file.length);
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
        response.setHeader("Content-Disposition", "attachment;filename=" + encodedFileName);
        OutputStream os = response.getOutputStream();
        os.write(file);
        result.put("success", "下载成功");
        return result.toString();
    }
}
