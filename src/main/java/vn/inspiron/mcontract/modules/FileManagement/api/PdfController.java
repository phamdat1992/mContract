package vn.inspiron.mcontract.modules.FileManagement.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import vn.inspiron.mcontract.modules.FileManagement.service.FileManageService;
import vn.inspiron.mcontract.modules.FileManagement.service.UrlService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.URL;

@RestController
public class PdfController {
//    private final static String PDF_URL_1 = "https://www.w3docs.com/uploads/media/default/0001/01/540cb75550adf33f281f29132dddd14fded85bfc.pdf";
//    private final static String PDF_URL_2 = "https://docs.spring.io/spring-boot/docs/current/reference/pdf/spring-boot-reference.pdf";

    @Autowired
    private UrlService urlService;

    @Autowired
    private FileManageService fileManageService;

    @GetMapping(value = "/generate_pdf_url")
    public ResponseEntity<String> getPdfFile(@RequestParam(value = "key")  String key) {
        String url = "localhost:9293" + urlService.generateExpirationUrl(key, "/pdf/");
        return ResponseEntity.ok(url);
    }

    @GetMapping("/pdf/{encrypt}")
    public ResponseEntity<byte[]> getPdf(@PathVariable String encrypt) {
        String fileName = urlService.getDataFromUrl(encrypt);
        if (fileName == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("url wrong".getBytes());
        }
        byte[] content = null;
        try {
        	content = fileManageService.getFileObject(fileName);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("get file fail".getBytes());
        }
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(content);
    }

    @PostMapping("/upload_pdf")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileManageService.uploadFile(file);
        
        if (fileName == null) {
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Upload fail");
        }

        return ResponseEntity.ok(fileName);
    }
    
    @DeleteMapping("/delete_pdf")
    public ResponseEntity<String> deleteFile(@RequestParam(value = "key")  String key) {
        fileManageService.deleteFileOnS3(key);
        return ResponseEntity.ok().body(key);
    }
}
