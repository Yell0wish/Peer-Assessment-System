package com.ye.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ye.dao.UserDao;
import com.ye.pojo.UserInformationPublicPojo;
import com.ye.pojo.UserPojo;
import com.ye.utils.PasswordHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.util.annotation.Nullable;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserDao userDao;

    public void signup(String email, String password){
        //对密码进行hash存储
        String password_hash = PasswordHash.getInstance().getMD5(password);
        userDao.insert(new UserPojo(email, password_hash));
    }

    @Nullable
    public UserPojo selectByEmail(String email){
        // 根据邮箱查询user表
        return userDao.selectOne(new QueryWrapper<UserPojo>().eq("email", email));
    }

    public UserPojo selectByID(int id){
        return userDao.selectById(id);
    }

    public void modifyName(UserPojo userPojo, String newName, int userid){
        userPojo.setUsername(newName);
        userDao.update(userPojo, new QueryWrapper<UserPojo>().eq("uuid", userid));
    }

    public void modifyPassword(UserPojo userPojo, String newPassword, int userid){
        userPojo.setPassword(PasswordHash.getInstance().getMD5(newPassword));
        userDao.update(userPojo, new QueryWrapper<UserPojo>().eq("uuid", userid));
    }

    public void modifyPassword(UserPojo userPojo, String newPassword, String email){
        userPojo.setPassword(PasswordHash.getInstance().getMD5(newPassword));
        userDao.update(userPojo, new QueryWrapper<UserPojo>().eq("email", email));
    }

    public void modifyIdentity(UserPojo userPojo, int newIdentity, int userid){
        userPojo.setIdentity(newIdentity);
        userDao.update(userPojo, new QueryWrapper<UserPojo>().eq("uuid", userid));
    }

    public void modifySchool(UserPojo userPojo, String newSchool, int userid){
        userPojo.setSchool(newSchool);
        userDao.update(userPojo, new QueryWrapper<UserPojo>().eq("uuid", userid));
    }

    public void modifySchoolCode(UserPojo userPojo, String newSchoolCode, int userid){
        userPojo.setSchoolCode(newSchoolCode);
        userDao.update(userPojo, new QueryWrapper<UserPojo>().eq("uuid", userid));
    }

    public void deleteUser(int userid) {
        userDao.deleteById(userid);
    }

    public UserInformationPublicPojo getPublicInformation(int userid){
        return userDao.getPublcInformation(userid);
    }

    public String getNameByID(int userid) {
        return userDao.selectById(userid).getUsername();
    }
}
