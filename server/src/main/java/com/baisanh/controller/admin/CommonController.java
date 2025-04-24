package com.baisanh.controller.admin;

import com.baisanh.constant.MessageConstant;
import com.baisanh.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * 通用接口
 */
@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {
    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile file){

        log.info("文件上传：{}", file);
        try {
            // 原始文件名
            String originalFilename = file.getOriginalFilename();
            // 截取原始文件名的后缀   xxx.png
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            // 构造新文件名称
            String objectName = UUID.randomUUID().toString() + extension;

            // 定义前端项目public目录下的存储路径
            String uploadDir = "D:\\IDEA_Project\\Capstone\\cursor_sky\\public\\admin-images"; // 前端项目的public/admin-images目录
            Path uploadPath = Paths.get(uploadDir);

            // 确保上传目录存在
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 构造完整的文件路径
            Path filePath = uploadPath.resolve(objectName);

            // 保存文件到前端项目目录
            file.transferTo(filePath.toFile());

            // 返回文件的相对路径（相对于public目录）
            return Result.success("admin-images/" + objectName);
        } catch (IOException e) {
            log.error("文件上传失败：{}", e);
        }

        return Result.error(MessageConstant.UPLOAD_FAILED);
    }

    /**
     * 提供图片文件 (可以保留，但前端会直接使用public目录中的文件)
     * @param filename
     * @return
     */
    @GetMapping("/images/{filename:.+}")
    @ApiOperation("提供图片文件")
    public ResponseEntity<Resource> serveImage(@PathVariable String filename) {
        try {
            // 定义前端项目public目录下的存储路径
            String uploadDir = "D:\\IDEA_Project\\Capstone\\cursor_sky\\public\\admin-images";
            Path filePath = Paths.get(uploadDir, filename);

            Resource resource = new FileSystemResource(filePath.toFile());
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(determineMediaType(filename)) // 根据文件扩展名确定类型
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("提供图片文件失败：{}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 根据文件扩展名确定媒体类型
     */
    private MediaType determineMediaType(String filename) {
        if (filename.endsWith(".png")) {
            return MediaType.IMAGE_PNG;
        } else if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
            return MediaType.IMAGE_JPEG;
        } else if (filename.endsWith(".gif")) {
            return MediaType.IMAGE_GIF;
        } else {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}