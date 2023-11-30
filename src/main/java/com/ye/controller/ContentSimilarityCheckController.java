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
import com.ye.utils.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@RestController
public class ContentSimilarityCheckController {
    @RequestMapping(value = "/ContentCheck", method = RequestMethod.POST)
    public String ContentCheck(HttpServletResponse response,
                               @RequestParam("fileA") MultipartFile fileA,
                               @RequestParam("fileB") MultipartFile fileB) throws IOException, InterruptedException {
        // 只允许pdf word excel txt rtf文件
        if (fileA.getContentType() == null && !(fileA.getContentType().contains("pdf") || fileA.getContentType().contains("word") || fileA.getContentType().contains("excel") || fileA.getContentType().contains("txt") || fileA.getContentType().contains("rtf"))) {
            return Result.defeat("文件A格式不正确");
        }
        if (fileB.getContentType() == null && !(fileB.getContentType().contains("pdf") || fileB.getContentType().contains("word") || fileB.getContentType().contains("excel") || fileB.getContentType().contains("txt") || fileB.getContentType().contains("rtf"))) {
            return Result.defeat("文件B格式不正确");
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
        CheckManager.INSTANCE.setRegCode("wNapGy/uhgE+rv5BsW4gcSiL0qFHV1/xXVNbuKI91TdLKAKW7afYGrQD9YDRBQC6ixvYQuMDj2iBNF4CQ+oLWcWWQBV+kSpX/s788zBOvN7vfhdP/L0Neu17B3dulsPT3cMp0TzSSzOBRsCGA5IE5ipAdFLczrREL0UEdjxJivok1HNzTdllFHvw4Nxnm7iMp/H/g6T28QzQPhV2rNRxZ6fBZMPcXzpCf/Uk9fnh/XBCkFXEypcPZV65/mY5lMTA9KgK8MrcQmt63ARYx+P5GjMBI5Lvob9YCiQF4iGdi5S2+tmciqrOpnb7uZ8//XQoXTisxgLWAP40rT7/mmSsQ54Y3V1op2I5iU7wMMCIpklJC54TzTvXvPfmUcwx+lqZQ1EpPNoHQ9hbJq/05EgIgw/lJM2vUAbyHHfz8ShsfeGRJXQjIIxS1Sil3XMBABWp4buR34onla9RICq60+BthP9SaB8unCQ2S0ymxL9DRUo=");
        EasyStarter.check(new File(localPathA), new File(localPathB), localResult);
        return Result.success("result.zip", FileConversionUtil.zipFolder(localResult), response);

    }


}
