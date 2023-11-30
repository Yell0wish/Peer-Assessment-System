package com.ye.utils;

import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileConversionUtil {
    public static File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        file.transferTo(convertedFile);
        return convertedFile;
    }

    public static void saveMultipartFileToLocal(MultipartFile multipartFile, String localFilePath) throws IOException {
        File file = new File(localFilePath);
        multipartFile.transferTo(file);
    }

    public static void deleteFolderContents(File folder) throws IOException {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteFolderContents(file);
                } else {
                    FileUtils.forceDelete(file);
                }
            }
        }
    }

    public static void zipFolder(String sourceFolderPath, String zipFilePath) throws IOException {
        try (
                FileOutputStream fos = new FileOutputStream(zipFilePath);
                ZipOutputStream zipOut = new ZipOutputStream(fos)
        ) {
            File sourceFolder = new File(sourceFolderPath);
            zipFile(sourceFolder, sourceFolder.getName(), zipOut);
        }
    }

    public static byte[] zipFolder(String sourceFolderPath) throws IOException {
        try (
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ZipOutputStream zipOut = new ZipOutputStream(baos)
        ) {
            File sourceFolder = new File(sourceFolderPath);
            zipFile(sourceFolder, sourceFolder.getName(), zipOut);
            zipOut.finish();
            return baos.toByteArray();
        }
    }

    private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }

        if (fileToZip.isDirectory()) {
            String[] children = fileToZip.list();
            for (String child : children) {
                zipFile(new File(fileToZip, child), fileName + "/" + child, zipOut);
            }
        } else {
            try (
                    FileInputStream fis = new FileInputStream(fileToZip)
            ) {
                ZipEntry zipEntry = new ZipEntry(fileName);
                zipOut.putNextEntry(zipEntry);

                byte[] bytes = new byte[1024];
                int length;
                while ((length = fis.read(bytes)) >= 0) {
                    zipOut.write(bytes, 0, length);
                }
            }
        }
    }

}
