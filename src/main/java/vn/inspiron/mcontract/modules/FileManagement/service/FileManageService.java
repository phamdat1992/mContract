package vn.inspiron.mcontract.modules.FileManagement.service;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Service
public class FileManageService {
	private final static long MAX_PART_SIZE = 5242880;

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
				.enablePathStyleAccess()
				.withClientConfiguration(config)
				.build();
		return s3Client;
	}

	public void uploadFile(String fileName, byte[] fileData) throws Exception {
		try {
			File convertedFile = this.convertFileDataToFile(fileName, fileData);
			this.uploadUsingS3Upload(convertedFile, fileName);
		} catch (IOException e) {
			throw new Exception("file is wrong");
		} catch (Exception e) {
			throw new Exception("upload fail");
		}
	}

	protected void uploadUsingS3Upload(File file, String keyName) throws Exception {
		ArrayList<PartETag> partETags = new ArrayList<PartETag>();
		InitiateMultipartUploadResult initResponse = null;
		try {
			initResponse = this.sendInitiateMultipartUploadRequest(keyName);
			this.uploadFileS3(file, initResponse.getUploadId(), keyName, partETags);
			this.sendCompleteMultipartUploadRequest(initResponse.getUploadId(), keyName, partETags);
		} catch (Exception e) {
			if (initResponse != null) {
				s3Client.abortMultipartUpload(
						new AbortMultipartUploadRequest(
								bucketName,
								keyName,
								initResponse.getUploadId()
						)
				);
			}
			e.printStackTrace();
			throw e;
		}

	}

	protected InitiateMultipartUploadResult sendInitiateMultipartUploadRequest(String keyName) {
		InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(bucketName, keyName);
		return s3Client.initiateMultipartUpload(initRequest);
	}

	protected void sendCompleteMultipartUploadRequest(
			String uploadId,
			String keyName,
			ArrayList<PartETag> partETags
	) {
		CompleteMultipartUploadRequest compRequest = new CompleteMultipartUploadRequest(
				bucketName,
				keyName,
				uploadId,
				partETags);
		s3Client.completeMultipartUpload(compRequest);
	}

	protected void uploadFileS3(
			File file,
			String uploadId,
			String keyName,
			ArrayList<PartETag> partETags
	) {
		long filePosition = 0;
		long contentLength = file.length();
		long partSize = MAX_PART_SIZE;
		for (int i = 1; filePosition < contentLength; i++) {
			// Last part can be less than 5 MB. Adjust part size.
			partSize = Math.min(partSize, (contentLength - filePosition));

			// Create request to upload a part.
			UploadPartRequest uploadRequest = new UploadPartRequest()
					.withBucketName(bucketName).withKey(keyName)
					.withUploadId(uploadId).withPartNumber(i)
					.withFileOffset(filePosition)
					.withFile(file)
					.withPartSize(partSize);

			// Upload part and add response to our list.
			UploadPartResult result = s3Client.uploadPart(uploadRequest);

			partETags.add(result.getPartETag());

			filePosition += partSize;
		}
	}

	protected File convertFileDataToFile(String fileName, byte[] fileData) throws IOException {
		File convertedFile = new File(Objects.requireNonNull(fileName));
		FileOutputStream fileOutputStream = new FileOutputStream(convertedFile);
		fileOutputStream.write(fileData);
		fileOutputStream.close();
		return convertedFile;
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

	public boolean isPDF(byte[] data) {
		if (data != null && data.length > 4 &&
				data[0] == 0x25 && // %
				data[1] == 0x50 && // P
				data[2] == 0x44 && // D
				data[3] == 0x46 && // F
				data[4] == 0x2D) { // -

			// version 1.3 file terminator
			if (data[5] == 0x31 && data[6] == 0x2E && data[7] == 0x33 &&
					data[data.length - 7] == 0x25 && // %
					data[data.length - 6] == 0x25 && // %
					data[data.length - 5] == 0x45 && // E
					data[data.length - 4] == 0x4F && // O
					data[data.length - 3] == 0x46 && // F
					data[data.length - 2] == 0x20 && // SPACE
					data[data.length - 1] == 0x0A) { // EOL
				return true;
			}

			// version 1.3 file terminator
			if (data[5] == 0x31 && data[6] == 0x2E && data[7] == 0x34 &&
					data[data.length - 6] == 0x25 && // %
					data[data.length - 5] == 0x25 && // %
					data[data.length - 4] == 0x45 && // E
					data[data.length - 3] == 0x4F && // O
					data[data.length - 2] == 0x46 && // F
					data[data.length - 1] == 0x0A) { // EOL
				return true;
			}
		}
		return false;
	}
}
