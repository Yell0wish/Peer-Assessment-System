package com.ye.service;

import com.ye.dao.InformationDao;
import com.ye.pojo.InformationPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InformationService {
    @Autowired
    InformationDao informationDao;

    public void sendInformation(int userid, String content){
        InformationPojo informationPojo = new InformationPojo();
        informationPojo.setUserid(userid);
        informationPojo.setContent(content);
        informationDao.insert(informationPojo);
    }

}
