package com.lyl.layuiadmin.service.tencentCos;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.COSEncryptionClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.auth.COSStaticCredentialsProvider;
import com.qcloud.cos.internal.crypto.*;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

/**
 * 加密上传文件及下载加密文件
 */
public class SymmetricKeyEncryptionClient {

    private static final String keyFilePath = "E:/xx/mydata/secret.key";

    public static String getKeyFilePath() {
        URL url = SymmetricKeyEncryptionClient.class.getClassLoader().getResource("secret.key");
        return url.getFile();
    }

    // 产生和保存秘钥信息的示例, 推荐使用256位秘钥.
    public static void buildAndSaveSymmetricKey() throws IOException, NoSuchAlgorithmException {
        // Generate symmetric 256 bit AES key.
        KeyGenerator symKeyGenerator = KeyGenerator.getInstance("AES");
        // JDK默认不支持256位长度的AES秘钥, SDK内部默认使用AES256加密数据
        // 运行时会打印如下异常信息java.security.InvalidKeyException: Illegal key size or default parameters
        // 解决办法参考接口文档的FAQ
        symKeyGenerator.init(256);
        SecretKey symKey = symKeyGenerator.generateKey();

        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(symKey.getEncoded());
        FileOutputStream keyfos = new FileOutputStream(getKeyFilePath());
        keyfos.write(x509EncodedKeySpec.getEncoded());
        keyfos.close();
    }

    // 加载秘钥的示例
    public static SecretKey loadSymmetricAESKey() throws IOException, NoSuchAlgorithmException,
            InvalidKeySpecException, InvalidKeyException {
        // Read private key from file.
        File keyFile = new File(getKeyFilePath());
        FileInputStream keyfis = new FileInputStream(keyFile);
        byte[] encodedPrivateKey = new byte[(int) keyFile.length()];
        keyfis.read(encodedPrivateKey);
        keyfis.close();
        // Generate secret key.
        return new SecretKeySpec(encodedPrivateKey, "AES");
    }

    /**
     * 生成成加密客户端EncryptionClient
     *
     * @param secretId
     * @param secretKey
     * @param region    区域
     * @return
     * @throws Exception
     */
    public static COSEncryptionClient getCOSEncryptionClient(String secretId, String secretKey, String region) throws Exception {
        // 初始化用户身份信息(secretId, secretKey)
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        // 设置bucket的区域, COS地域的简称请参照 https://www.qcloud.com/document/product/436/6224
        ClientConfig clientConfig = new ClientConfig(new Region(region));

        // 加载保存在文件中的秘钥, 如果不存在，请先使用buildAndSaveSymmetricKey生成秘钥
        //buildAndSaveSymmetricKey();
        SecretKey symKey = loadSymmetricAESKey();

        EncryptionMaterials encryptionMaterials = new EncryptionMaterials(symKey);
        // 使用AES/GCM模式，并将加密信息存储在文件元信息中.
        CryptoConfiguration cryptoConf = new CryptoConfiguration(CryptoMode.AuthenticatedEncryption)
                .withStorageMode(CryptoStorageMode.ObjectMetadata);

        // 生成加密客户端EncryptionClient, COSEncryptionClient是COSClient的子类, 所有COSClient支持的接口他都支持。
        // EncryptionClient覆盖了COSClient上传下载逻辑，操作内部会执行加密操作，其他操作执行逻辑和COSClient一致
        COSEncryptionClient cosEncryptionClient =
                new COSEncryptionClient(new COSStaticCredentialsProvider(cred),
                        new StaticEncryptionMaterialsProvider(encryptionMaterials), clientConfig,
                        cryptoConf);
        return cosEncryptionClient;
    }

    public static void main(String[] args) {
        COSCredentials cred = new BasicCOSCredentials("secretId", "secretKey");
        ClientConfig clientConfig = new ClientConfig(new Region("ap-shanghai"));
        COSClient cosClient = new COSClient(cred, clientConfig);
        // 方法1 本地文件上传
        String key = "20191127/001/1516002325.jpg";
        File localFile = new File("E:/xx/temp/1516002325.jpg");
        PutObjectResult putObjectResult = cosClient.putObject("bucketName", key, localFile);
        String etag = putObjectResult.getETag();  // 获取文件的 etag
        System.out.println(etag);
        cosClient.shutdown();
    }
}
