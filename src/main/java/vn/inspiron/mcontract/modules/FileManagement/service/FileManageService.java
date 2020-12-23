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
import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vn.inspiron.mcontract.modules.FileManagement.dto.FileBase64DTO;

import javax.annotation.PostConstruct;
import java.io.*;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.*;

@Service
public class FileManageService {
	private final String AUTHOR = "author";
	private final String PHONE = "phone";
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
			InputStream uploadStream = new ByteArrayInputStream(fileData);
			ObjectMetadata metadata = new ObjectMetadata();
			this.s3Client.putObject(this.bucketName, fileName, uploadStream, metadata);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("upload fail");
		}
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

	public FileBase64DTO convertBase64WordToPDF(FileBase64DTO wordBase64) throws IOException {
		byte[] fileData = Base64.getDecoder().decode(wordBase64.getData());
		InputStream inFile = new ByteArrayInputStream(fileData);
		XWPFDocument doc = new XWPFDocument(inFile);
		PdfOptions pdfOptions = PdfOptions.create();
		OutputStream binaryPDF = new ByteArrayOutputStream();
		PdfConverter.getInstance().convert(doc, binaryPDF, pdfOptions);

		try (ByteArrayOutputStream outData = (ByteArrayOutputStream) binaryPDF) {
			String base64PDF = Base64.getEncoder().encodeToString(outData.toByteArray());
			return new FileBase64DTO(base64PDF);
		}
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

			// version 1.7
			// EOL
			if (data[5] == 0x31 && data[6] == 0x2E && data[7] == 0x35 &&
					data[data.length - 6] == 0x25 && // %
					data[data.length - 5] == 0x25 && // %
					data[data.length - 4] == 0x45 && // E
					data[data.length - 3] == 0x4F && // O
					data[data.length - 2] == 0x46 && // F
					data[data.length - 1] == 0x0A) {
				return true;
			}

			return data[5] == 0x31 && data[6] == 0x2E && data[7] == 0x35;
		}
		return false;
	}
}
