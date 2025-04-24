package com.baisanh.utils;

import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FileUtil {

    private static final String UPLOAD_DIR = "D:\\IDEA_Project\\Capstone\\userUpload";

    public static String upload(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("上传文件不能为空");
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        File dest = new File(UPLOAD_DIR + fileName);

        try {
            file.transferTo(dest);
            return "/upload/" + fileName; // 返回访问路径
        } catch (IOException e) {
            throw new RuntimeException("文件上传失败", e);
        }
    }
}