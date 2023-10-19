package com.ye.service;

import com.ye.utils.RandomProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    JavaMailSender javaMailSender;

    public String sendCheckEmail(String receiver) {
        String checkcode = RandomProduct.getCheckCode();

        Thread emailThread = new Thread(() -> {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setText("尊敬的用户,您好:\n"
                    + "\n本次请求的邮件验证码为:" + checkcode + ",本验证码 10 分钟内效，请及时输入。（请勿泄露此验证码）\n"
                    + "\n如非本人操作，请忽略该邮件。\n(这是一封通过自动发送的邮件，请不要直接回复)\n"
                    + "\nYellowish团队");
            mailMessage.setSubject("PeerSystem CheckCode");
            mailMessage.setFrom("3104812631@qq.com");
            mailMessage.setTo(receiver);
            javaMailSender.send(mailMessage);
        });

        emailThread.start(); // 启动线程执行邮件发送

        return checkcode;
    }
}

