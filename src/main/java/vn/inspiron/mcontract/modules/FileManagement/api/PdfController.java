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
import java.util.HashMap;
import java.util.Map;

@RestController
public class PdfController {
    @Autowired
    private UrlService urlService;

    @Autowired
    private FileManageService fileManageService;

    @GetMapping(value = "/generate_pdf_url")
    public ResponseEntity<Object> getPdfFile(@RequestParam(value = "key")  String key) {
    	HashMap<String, Object> responseBody = new HashMap<String, Object>();
        String url = "localhost:9293/pdf/" + urlService.generateExpirationCode(key);
        responseBody.put("key", key);
        responseBody.put("lifetime", "10 minutes");
        responseBody.put("pdf_url", url);
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/pdf/{encrypt}")
    public ResponseEntity<Object> getPdf(@PathVariable String encrypt) {
        String fileName = urlService.getDataFromUrl(encrypt);
        if (fileName == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("wrong key name".getBytes());
        }
        byte[] content = null;
        try {
        	content = fileManageService.getFileObject(fileName);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("download fail".getBytes());
        }
        return ResponseEntity.ok()
        		.contentType(MediaType.APPLICATION_PDF)
        		.body(content);
    }

    @PostMapping("/upload_pdf")
    public ResponseEntity<Object> uploadFile(@RequestParam("file") MultipartFile file) {
    	Map<String, Object> responseBody = fileManageService.uploadFile(file);
        
        if (responseBody.containsKey("exception")) {
        	HttpStatus httpStatus = (HttpStatus) responseBody.get("exception");
        	return ResponseEntity.status(httpStatus).body(responseBody);
        }

        return ResponseEntity.ok(responseBody);
    }
    
    @DeleteMapping("/delete_pdf")
    public ResponseEntity<String> deleteFile(@RequestParam(value = "key")  String key) {
        fileManageService.deleteFileOnS3(key);
        return ResponseEntity.ok().body(key);
    }
}
