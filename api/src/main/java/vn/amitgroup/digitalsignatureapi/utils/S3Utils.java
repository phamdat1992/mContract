package vn.amitgroup.digitalsignatureapi.utils;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class S3Utils {
    @Value("${s3.Properties.endpointUrl}")
    private String endpointUrl;
    @Value("${s3.Properties.bucketName}")
    private String bucketName;
    @Value("${s3.Properties.accessKey}")
    private String accessKey;
    @Value("${s3.Properties.secretKey}")
    private String secretKey;

    private AmazonS3 s3Client;

    @PostConstruct
    public void initializeS3() {
        this.s3Client = initS3Client();
        createBucket(this.s3Client, bucketName);
    }

    private AmazonS3 initS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(
                accessKey,
                secretKey
        );
        ClientConfiguration config = new ClientConfiguration();
        config.setSignerOverride("S3SignerType");
        AmazonS3 s3Client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(
                                endpointUrl,
                                null
                        )
                )
                .withPathStyleAccessEnabled(true)
                .withClientConfiguration(config)
                .build();
        return s3Client;
    }

    public void createBucket(AmazonS3 s3Client, String bucketName){
        try {
            if (!s3Client.doesBucketExistV2(bucketName)) {
                // Because the CreateBucketRequest object doesn't specify a region, the
                // bucket is created in the region specified in the client.
                s3Client.createBucket(new CreateBucketRequest(bucketName));
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public String getFileUrl(String keyName) throws Exception {
        String s3Url = s3Client.getUrl(this.bucketName, keyName).toExternalForm();
        return s3Url;
    }

    public String uploadFile(String keyName, byte[] fileData) throws Exception {
        InputStream uploadStream = new ByteArrayInputStream(fileData);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(fileData.length);
        s3Client.putObject(this.bucketName, keyName, uploadStream, metadata);
        return keyName;
    }

    public String uploadFile(String keyName, InputStream uploadStream) throws Exception {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(uploadStream.available());
        AccessControlList acl = new AccessControlList();
        acl.grantPermission(GroupGrantee.AllUsers, Permission.Read); //all users or authenticated
        s3Client.putObject(this.putPublicObject(bucketName, keyName, uploadStream, metadata));
        return keyName;
    }

    public byte[] getFileObject(String keyName) throws IOException {
        byte[] content = null;
        S3Object s3Object = s3Client.getObject(bucketName, keyName);
        S3ObjectInputStream is = s3Object.getObjectContent();
        content = IOUtils.toByteArray(is);
        s3Object.close();
        return content;
    }

    public String deleteFileOnS3(String keyName) {
        s3Client.deleteObject(bucketName, keyName);
        return keyName;
    }

    public String getPresignedUrl(String bucketName, String keyName, Long expiredTimeMilliSeconds) throws Exception{
        // Set the presigned URL to expire after expiredTimeMilliSeconds.
        long expTimeMillis = Instant.now().toEpochMilli();
        Date expiration = new Date();
        expTimeMillis += expiredTimeMilliSeconds;
        expiration.setTime(expTimeMillis);

        // Generate the presigned URL.
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, keyName)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(expiration);
        URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toExternalForm();
    }

    private PutObjectRequest putPublicObject(String bucketName, String keyName, InputStream uploadStream, ObjectMetadata metadata){
        PutObjectRequest objectRequest = new PutObjectRequest(this.bucketName, keyName, uploadStream, metadata);
        AccessControlList accessControlList = new AccessControlList();
        accessControlList.grantPermission(GroupGrantee.AllUsers, Permission.Read); //all users or authenticated
        objectRequest.setAccessControlList(accessControlList);
        return objectRequest;
    }

    public List<String> getSameBucketPresignedUrls(String bucketName, List<String> keyNames, Long expiredTimeMilliSeconds) throws Exception{
        // Set the presigned URL to expire after expiredTimeMilliSeconds.
        long expTimeMillis = Instant.now().toEpochMilli();
        Date expiration = new Date();
        List<String> urls = new ArrayList<>();
        expTimeMillis += expiredTimeMilliSeconds;
        expiration.setTime(expTimeMillis);

        // Generate the presigned URL.
        for (String keyName: keyNames){
            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(bucketName, keyName)
                            .withMethod(HttpMethod.GET)
                            .withExpiration(expiration);
            URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
            urls.add(url.toExternalForm());
        }
        return urls;
    }
}
