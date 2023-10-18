package com.ye.controller;

import com.ye.pojo.ClassPojo;
import com.ye.pojo.FilePojo;
import com.ye.pojo.UserPojo;
import com.ye.service.ClassService;
import com.ye.service.ClassSourceService;
import com.ye.service.UserService;
import com.ye.utils.PasswordHash;
import com.ye.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
public class ClassSourceController {
    @Autowired
    ClassService classService;

    @Autowired
    UserService userService;

    @Autowired
    ClassSourceService classSourceService;

    @RequestMapping(value = "/addClassSource", method = RequestMethod.POST)
    public String addClassSource(@RequestParam("userid") int userid,
                                 @RequestParam("token") String token,
                                 @RequestParam("classid") int classid,
                                 @RequestParam("sourceName") String sourceName,
                                 @RequestParam("attachment") MultipartFile attachment) {

        UserPojo userPojo = userService.selectByID(userid);
        if (userPojo == null) {
            return Result.defeat("用户ID不存在");
        } else if (!token.equals(PasswordHash.getInstance().getMD5(userPojo.getEmail()))) {
            return Result.defeat("token不正确");
        } else {
            ClassPojo classPojo = classService.selectClassByID(classid);
            if (classPojo == null) {
                return Result.defeat("课程不存在");
            } else if (classPojo.getUserid() == userid) { //正常操作
                if (attachment.isEmpty()){
                    return Result.defeat("文件不能为空");
                }
                classSourceService.addClassSource(classid, sourceName, attachment);
                return Result.success("您已成功添加资源");
            } else {
                return Result.defeat("您不是班级拥有者");
            }
        }
    }

    @RequestMapping(value = "/getFile", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getFile(
            @RequestParam("userid") int userid,
            @RequestParam("token") String token,
            @RequestParam("classid") int classid,
            @RequestParam("sourceid") int sourceid) throws UnsupportedEncodingException {

        UserPojo userPojo = userService.selectByID(userid);
        if (userPojo == null) {
            return ResponseEntity.badRequest().body("用户ID不存在".getBytes());
        } else if (!token.equals(PasswordHash.getInstance().getMD5(userPojo.getEmail()))) {
            return ResponseEntity.badRequest().body("token不正确".getBytes());
        } else {
            ClassPojo classPojo = classService.selectClassByID(classid);
            if (classPojo == null) {
                return ResponseEntity.badRequest().body("课程不存在".getBytes());
            } else if (classPojo.getUserid() == userid) { // 正常操作
                FilePojo resource = classSourceService.getResource(sourceid);
                if (resource != null && resource.getFile() != null) {
                    System.out.println(resource.getFileName());
                    byte[] fileData = resource.getFile(); // 获取文件的字节数组
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                    headers.setContentDispositionFormData("file", URLEncoder.encode(resource.getFileName(), StandardCharsets.UTF_8)); // 指定下载的文件名

                    return new ResponseEntity<>(fileData, headers, HttpStatus.OK);
                } else {
                    return ResponseEntity.notFound().build();
                }
            } else {
                // todo 验证是不是班级的学生
                return ResponseEntity.badRequest().body("您不是班级拥有者".getBytes());
            }
        }
    }

}
