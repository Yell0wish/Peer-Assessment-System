package com.ye.controller;

import cn.textcheck.CheckManager;
import cn.textcheck.engine.checker.CheckTask;
import cn.textcheck.engine.pojo.LocalPaperLibrary;
import cn.textcheck.engine.pojo.Paper;
import cn.textcheck.engine.report.Reporter;
import cn.textcheck.engine.type.ReportType;
import cn.textcheck.starter.EasyStarter;
import com.ye.pojo.ClassPojo;
import com.ye.pojo.FilePojo;
import com.ye.pojo.UserPojo;
import com.ye.utils.FileConversionUtil;
import com.ye.utils.RandomProduct;
import com.ye.utils.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
public class ContentSimilarityCheckController {
    // 定义一个对象用于作为锁
    private static final Object lock = new Object();
    private static String password = "";
    @RequestMapping(value = "/ContentCheck", method = RequestMethod.POST)
    public String ContentCheck(@RequestParam("fileA") MultipartFile fileA,
                               @RequestParam("fileB") MultipartFile fileB) throws IOException, InterruptedException {
        synchronized (lock) {        // 只允许pdf word excel txt rtf文件
            if (fileA.getContentType() == null || !(fileA.getContentType().contains("pdf") || fileA.getContentType().contains("word") || fileA.getContentType().contains("excel") || fileA.getContentType().contains("txt") || fileA.getContentType().contains("rtf"))) {
                return Result.defeat("文件A格式不正确, 只允许pdf word excel txt rtf文件");
            }
            if (fileB.getContentType() == null || !(fileB.getContentType().contains("pdf") || fileB.getContentType().contains("word") || fileB.getContentType().contains("excel") || fileB.getContentType().contains("txt") || fileB.getContentType().contains("rtf"))) {
                return Result.defeat("文件B格式不正确, 只允许pdf word excel txt rtf文件");
            }
            String localPathA = "C:\\Users\\Yellowish\\Desktop\\new\\testA";
            String localPathB = "C:\\Users\\Yellowish\\Desktop\\new\\testB";
            String localResult = "C:\\Users\\Yellowish\\Desktop\\new\\result";
            String localZipResult = "C:\\Users\\Yellowish\\Desktop\\new\\result.zip";
            String fileAPath = localPathA + "\\" + fileA.getOriginalFilename();
            String fileBPath = localPathB + "\\" + fileB.getOriginalFilename();
            FileConversionUtil.deleteFolderContents(new File(localResult));
            FileConversionUtil.deleteFolderContents(new File(localPathA));
            FileConversionUtil.deleteFolderContents(new File(localPathB));
            FileConversionUtil.saveMultipartFileToLocal(fileA, fileAPath);
            FileConversionUtil.saveMultipartFileToLocal(fileB, fileBPath);


            FileConversionUtil.deleteFolderContents(new File(localResult));
            CheckManager.INSTANCE.setRegCode("D34auBYNDhIuC5PRqI75ItmgMjmtrSGQHFqutQtUQuJ2XDoSWZwdd28SQHW3iZz/gkAwO/Y5Y519XtXlZqCkBNLlCKjThrVDyC7Wkg7gA8s/f9lXMZFXcuq/6xPD/hUEifz/KggBWzEnnKXuXDrDWAOtqEEN7GiL1qcWALTANtypiuOHNc75wKnCITPjp8igQJiC/nco6WuiJa3JQhvPcQ3pX9I1Hv6wKiCmbAUGEihLHyMcnY3xCir1bRUUznhYmKl1h8ACVB3Cgbv18FN+AsAeFrehmaPF7FqMZvWjpM+dCa8KKLGmDCZH6k1mP2tre9dLGA0ar9YLl/K3OHr8f6ojJdp2xkAWuDrNKt0KzVqcsbLR/x/F/8cpGhkDkVJgC5AT6gl4FdV8YmLu5bRivlQo/FwSM+5itFhyDFd+yGjcWUXeA31d2UDoQ5wy33WxyLwQm3H7P6dU6rTqc2fqy98uToQZesMe2sBoU3EAUfE=");
            EasyStarter.check(new File(localPathA), new File(localPathB), localResult);
            FileConversionUtil.zipFolder(localResult, localZipResult);

            password = RandomProduct.getCheckCode();
            Map<String, Object> map = new HashMap<>();
            map.put("token", password);
            return Result.success("成功查重", map);
        }

    }

    @RequestMapping(value = "/getContentCheck", method = RequestMethod.GET)
    public String getContentCheck(HttpServletResponse response,
                                  @RequestParam("checkToken") String checkToken) throws IOException, InterruptedException {
        if (!checkToken.equals(password)) {
            return Result.defeat("checkToken不正确");
        }
        byte[] bytes = Files.readAllBytes(Paths.get("C:\\Users\\Yellowish\\Desktop\\new\\result.zip"));
        return Result.success("result.zip", bytes, response);
    }


}
