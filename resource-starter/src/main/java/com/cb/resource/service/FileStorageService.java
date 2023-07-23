package com.cb.resource.service;

import io.minio.errors.MinioException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

public interface FileStorageService {

    /**
     * 删除文件
     *
     * @param fileName   文件名
     * @param bucketName 桶名称
     * @throws MinioException 文件不存在
     */
    void removeFile(String fileName, String bucketName) throws MinioException;

    /**
     * 上传文件
     *
     * @param multipartFile 文件
     * @return 上传生成的文件名
     * @throws MinioException minio异常
     * @throws IOException    字节流为空
     */
    String uploadImg(MultipartFile multipartFile) throws MinioException, IOException;

    /**
     * 下载文件,使用response
     *
     * @param filePath   文件路径
     * @param bucketName 桶名称
     * @param response   输出的响应体
     * @throws MinioException 文件不存在
     * @throws IOException    response为空
     */
    void download(String filePath, String bucketName, HttpServletResponse response) throws MinioException, IOException;

    /**
     * 下载文件，需要手动关闭流，否则会一直占用资源
     *
     * @param filePath   文件名(包括路径)
     * @param bucketName 桶名称
     * @return 输入流，直接复制给响应体就可以了
     * @throws MinioException 获取失败，文件可能不存在
     */
    InputStream download(String filePath, String bucketName) throws MinioException;

}
