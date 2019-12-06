package com.lyl.layuiadmin.service.impl.tencentCos;

import com.lyl.layuiadmin.service.tencentCos.TencentCosSrevice;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.transfer.Download;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.Upload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class TencentCosSreviceImpl implements TencentCosSrevice , InitializingBean {

    @Value("${qcloud.appId}")
    private String appId;
    @Value("${qcloud.secretId}")
    private String secretId;
    @Value("${qcloud.secretKey}")
    private String secretKey;
    // 设置bucket所在的区域,上海（ap-shanghai）
    @Value("${qcloud.region}")
    private String region;
    @Value("${qcloud.originUrl}")
    private String originUrl;
    @Value("${qcloud.bucketName}")
    private String bucketName;

    private COSClient cosClient;
    TransferManager transferManager;

    /**
     * 一般接口
     */
    public void initCOSClient(){
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        ClientConfig clientConfig = new ClientConfig(new Region(region));
        cosClient = new COSClient(cred, clientConfig);
    }

    /**
     * 高级接口
     */
    public void initTransferManager(){
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        ClientConfig clientConfig = new ClientConfig(new Region(region));
        COSClient cosclient = new COSClient(cred, clientConfig);
        ExecutorService threadPool = Executors.newFixedThreadPool(32);
        transferManager = new TransferManager(cosclient, threadPool);
    }

    /**
     * 本地文件上传
     * @param key   上传文件目标路径
     * @param localFilePath  上传文件路径
     * @return
     */
    @Override
    public PutObjectResult upload4Local(String key, String localFilePath) {
        // 方法1 本地文件上传
        File localFile = new File(localFilePath);
        PutObjectResult putObjectResult = cosClient.putObject(bucketName, key, localFile);
        cosClient.shutdown();
        return putObjectResult;
    }

    /**
     * 输入流上传到
     * @param key   上传文件目标路径
     * @param localFilePath 上传文件路径
     * @param contentLength 输入流长度
     * @return
     */
    @Override
    public PutObjectResult upload4InputStream(String key, String localFilePath, Long contentLength) throws Exception{
        FileInputStream fileInputStream = new FileInputStream(localFilePath);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        // 设置输入流长度为500
        objectMetadata.setContentLength(contentLength);
        // 设置 Content type, 默认是 application/octet-stream
        //objectMetadata.setContentType("application/pdf");
        PutObjectResult putObjectResult = cosClient.putObject(bucketName, key, fileInputStream, objectMetadata);
        cosClient.shutdown();
        return putObjectResult;
    }

    /**
     * 更多细粒度的控制
     * @param key
     * @param localFilePath
     * @return
     */
    @Override
    public PutObjectResult putObject(String key, String localFilePath) {
        // 方法3 提供更多细粒度的控制, 常用的设置如下

        // 3 上传的同时指定权限(也可通过调用 API set object acl 来设置)
        // 4 若要全局关闭上传MD5校验, 则设置系统环境变量，此设置会对所有的会影响所有的上传校验。 默认是进行校验的。
        // 关闭MD5校验：  System.setProperty(SkipMd5CheckStrategy.DISABLE_PUT_OBJECT_MD5_VALIDATION_PROPERTY, "true");
        // 再次打开校验  System.setProperty(SkipMd5CheckStrategy.DISABLE_PUT_OBJECT_MD5_VALIDATION_PROPERTY, null);
        File localFile = new File(localFilePath);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);
        // storage-class 存储类型, 枚举值：Standard（标准），Standard_IA（低频），Archive（归档）。默认值：Standard
        putObjectRequest.setStorageClass(StorageClass.Standard);
        // 设置自定义属性(如 content-type, content-disposition 等)
        ObjectMetadata objectMetadata = new ObjectMetadata();
        // 2 content-type, 对于本地文件上传, 默认根据本地文件的后缀进行映射，如 jpg 文件映射 为image/jpeg
        //   对于流式上传 默认是 application/octet-stream
        // 设置 Content type, 默认是 application/octet-stream
        // objectMetadata.setContentType("image/jpeg");
        putObjectRequest.setMetadata(objectMetadata);
        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
        return putObjectResult;
    }

    /**
     * 高级接口上传
     * @param key   目标路径
     * @param localFilePath 上传文件路径
     * @return
     * @throws Exception
     */
    @Override
    public UploadResult upload(String key, String localFilePath) throws Exception{
        File localFile = new File(localFilePath);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);
        // 本地文件上传
        Upload upload = transferManager.upload(putObjectRequest);
        // 等待传输结束（如果想同步的等待上传结束，则调用 waitForCompletion）
        UploadResult uploadResult = upload.waitForUploadResult();

        /*// 本地文件上传
        PersistableUpload persistableUpload = null;
        Upload upload = transferManager.upload(putObjectRequest);
        // 等待"分块上传初始化"完成，并获取到 persistableUpload （包含uploadId等）
        while(persistableUpload == null) {
            persistableUpload = upload.getResumeableMultipartUploadId();
            Thread.sleep(100);
        }
        // 保存 persistableUpload
        // 步骤二：当由于网络等问题，大文件的上传被中断，则根据 PersistableUpload 恢复该文件的上传，只上传未上传的分块
        Upload newUpload = transferManager.resumeUpload(persistableUpload);
        // 等待传输结束（如果想同步的等待上传结束，则调用 waitForCompletion）
        UploadResult uploadResult = newUpload.waitForUploadResult();*/
        return uploadResult;
    }

    /**
     * 高级接口下载
     * @param key   源文件路径
     * @param localFilePath 下载目标路径
     * @throws Exception
     */
    @Override
    public void download(String key, String localFilePath) throws Exception{
        File localDownFile = new File(localFilePath);
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
        // 下载文件
        Download download = transferManager.download(getObjectRequest, localDownFile);
        // 等待传输结束（如果想同步的等待上传结束，则调用 waitForCompletion）
        download.waitForCompletion();
    }

    /**
     * 下载输入流
     * @param key   下载文件路径
     * @return
     */
    @Override
    public COSObjectInputStream downloadInputStream(String key) {
        // 方法1 获取下载输入流
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
        COSObject cosObject = cosClient.getObject(getObjectRequest);
        COSObjectInputStream cosObjectInput = cosObject.getObjectContent();
        cosClient.shutdown();
        return cosObjectInput;
    }

    @Override
    public ObjectMetadata getObject(GetObjectRequest getObjectRequest, File destinationFile) {
        return null;
    }

    @Override
    public ObjectMetadata getObjectMetadata(String key) {
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.initCOSClient();
        this.initTransferManager();
    }
}
