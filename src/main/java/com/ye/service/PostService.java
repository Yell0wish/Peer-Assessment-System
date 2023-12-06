package com.ye.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ye.dao.PostDao;
import com.ye.dao.ResponsePostDao;
import com.ye.pojo.PostPojo;
import com.ye.pojo.ResponsePostPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class PostService {
    @Autowired
    PostDao postDao;

    @Autowired
    ResponsePostDao  responsePostDao;
    public void publishPost(int classid, int userid, String title, String content, String pic, String name) {
        PostPojo postPojo = new PostPojo();
        postPojo.setClassID(classid);
        postPojo.setUserID(userid);
        postPojo.setTitle(title);
        postPojo.setContent(content);
        postPojo.setPic(pic);
        postPojo.setName(name);
        ZoneId zoneId = ZoneId.of("Asia/Shanghai"); // 指定"Asia/Shanghai"时区（北京时间）
        Date date = Date.from(LocalDateTime.now(zoneId).atZone(zoneId).toInstant());// 获取当前时间
        postPojo.setTime(date);

        postDao.insert(postPojo);

    }

    public List<PostPojo> getPostList(int classid) {
        return postDao.selectList(new QueryWrapper<PostPojo>().select("uuid", "title", "time", "name").eq("classID", classid));
    }

    public void publishComment(int userid, int postid, String content, String pic, String name) {
        ResponsePostPojo responsePostPojo = new ResponsePostPojo();
        responsePostPojo.setInformation(content);
        responsePostPojo.setPic(pic);
        ZoneId zoneId = ZoneId.of("Asia/Shanghai"); // 指定"Asia/Shanghai"时区（北京时间）
        Date date = Date.from(LocalDateTime.now(zoneId).atZone(zoneId).toInstant());// 获取当前时间
        responsePostPojo.setTime(date);
        responsePostPojo.setName(name);
        responsePostPojo.setPostID(postid);
        responsePostPojo.setUserID(userid);
        responsePostDao.insert(responsePostPojo);
    }

    public boolean existPost(int postid) {
        return postDao.selectById(postid) != null;
    }

    public List<PostPojo> getPostDetails(int postid) {
        return postDao.selectList(new QueryWrapper<PostPojo>().eq("uuid", postid));
    }

    public List<ResponsePostPojo> getPostComments(int postid) {
        return responsePostDao.selectList(new QueryWrapper<ResponsePostPojo>().eq("postID", postid));
    }
}
