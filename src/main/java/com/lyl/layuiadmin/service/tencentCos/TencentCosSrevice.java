package com.lyl.layuiadmin.service.tencentCos;

import com.qcloud.cos.model.*;

import java.io.File;

/**
 * 腾讯云存储
 */
public interface TencentCosSrevice {

    // 方法1  将本地文件上传到 COS
    public PutObjectResult upload4Local(String key, String localFilePath);

    // 方法2  输入流上传到 COS
    public PutObjectResult upload4InputStream(String key, String localFilePath,Long contentLength) throws Exception;

    // 方法3  对以上两个方法的包装, 支持更细粒度的参数控制, 如 content-type,  content-disposition 等
    public PutObjectResult putObject(String key, String localFilePath);

    // 上传对象
    public UploadResult upload(String key, String localFilePath) throws Exception;

    // 下载对象
    public void download(String key, String localFilePath) throws Exception;

    // 方法1 下载文件，并获取输入流
    public COSObjectInputStream downloadInputStream(String key);
    // 方法2 下载文件到本地.
    public ObjectMetadata getObject(GetObjectRequest getObjectRequest, File destinationFile);

    /**
     * 查询对象元数据
     */
    public ObjectMetadata getObjectMetadata(String key);
}
