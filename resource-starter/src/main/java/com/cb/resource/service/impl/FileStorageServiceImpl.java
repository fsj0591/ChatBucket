package com.cb.resource.service.impl;

import com.cb.resource.config.MinIOConfig;
import com.cb.resource.config.MinIOConfigProperties;
import com.cb.resource.service.FileStorageService;
import com.cb.resource.util.FileCheckUtil;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.MinioException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@EnableConfigurationProperties(MinIOConfigProperties.class)
@Import(MinIOConfig.class)
public class FileStorageServiceImpl implements FileStorageService {

    @Resource
    private MinioClient minioClient;

    @Resource
    private MinIOConfigProperties minIOConfigProperties;

    private final static String separator = "/";

    /**
     * 生成随机文件名
     *
     * @param fileName 原文件名
     * @return 新文件名
     */
    public String createFileName(String fileName) {
        int idx = fileName.lastIndexOf(".");
        String extention = fileName.substring(idx);
        return UUID.randomUUID().toString().replace("-", "") + extention;
    }

    @Override
    public void removeFile(String fileName, String bucketName) throws MinioException {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder().bucket(bucketName).object(fileName).build()
            );
        } catch (Exception e) {
            throw new MinioException("文件删除异常," + e.getMessage());
        }
    }

    @Override
    public String uploadImg(MultipartFile multipartFile) throws MinioException {
        try (
                InputStream inputStream = multipartFile.getInputStream()
        ) {
            // 判断图片后缀是否符合规范
            if (!FileCheckUtil.checkImageSuffix(Objects.requireNonNull(multipartFile.getOriginalFilename()))) {
                throw new MinioException("图片不合规");
            }
            String fileName = createFileName(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            minioClient.putObject(PutObjectArgs
                    .builder()
                    .bucket("image")
                    .object(fileName)
                    // 文件大小和分片大小，填-1默认为5Mib
                    .stream(inputStream, inputStream.available(), -1)
                    .contentType(multipartFile.getContentType())
                    .build());
            // 需要手动关闭输入流
            inputStream.close();
            // 返回文件路径
            StringBuilder urlPath = new StringBuilder(minIOConfigProperties.getReadPath());
            urlPath.append(separator+minIOConfigProperties.getBucket());
            urlPath.append(separator);
            urlPath.append(fileName);
            return urlPath.toString();
            } catch (Exception e) {
                e.printStackTrace();
                throw new MinioException("文件上传异常");
            }
    }

    /**
     * 下载文件,使用response
     *
     * @param filePath   文件路径
     * @param bucketName 桶名称
     * @param response   输出的响应体
     */
    @Override
    public void download(String filePath, String bucketName, HttpServletResponse response) throws MinioException, IOException {
        try (InputStream inputStream = this.download(filePath, bucketName)) {
            try (ServletOutputStream outputStream = response.getOutputStream()) {
                IOUtils.copy(inputStream, outputStream);
            }
        }
    }

    /**
     * 下载文件，需要手动关闭流，否则会一直占用资源
     *
     * @param filePath 文件名(包括路径)
     */
    @Override
    public InputStream download(String filePath, String bucketName) throws MinioException {
        try {
            return minioClient.getObject(GetObjectArgs
                    .builder()
                    .bucket(bucketName)
                    .object(filePath)
                    .build());
        } catch (Exception e) {
            throw new MinioException("文件下载异常," + e.getMessage());
        }
    }


}
