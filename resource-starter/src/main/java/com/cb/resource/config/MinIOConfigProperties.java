package com.cb.resource.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

@Data
@ConfigurationProperties(prefix = "minio")  // 文件上传 配置前缀file.oss
public class MinIOConfigProperties implements Serializable {


    /**
     * 访问名称
     */
    private String accessKey;

    /**
     * 访问密码
     */
    private String secretKey;

    /**
     * 桶
     */
    private String bucket;

    /**
     * 服务所在地址
     */
    private String endpoint;

    /**
     * 阅读路径
     */
    private String readPath;
}
