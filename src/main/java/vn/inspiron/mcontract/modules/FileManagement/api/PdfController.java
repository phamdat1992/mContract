package vn.inspiron.mcontract.modules.FileManagement.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import vn.inspiron.mcontract.modules.Authentication.dto.JwtTokenRequestDTO;
import vn.inspiron.mcontract.modules.Exceptions.BadRequest;
import vn.inspiron.mcontract.modules.Exceptions.ExpiredUrlException;
import vn.inspiron.mcontract.modules.FileManagement.dto.FileBase64DTO;
import vn.inspiron.mcontract.modules.FileManagement.service.FileManageService;
import vn.inspiron.mcontract.modules.FileManagement.service.UrlService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @PostMapping("/convert-to-pdf")
    public ResponseEntity<FileBase64DTO> convertWordToPDF(@RequestBody FileBase64DTO file, HttpServletRequest request, HttpServletResponse response) {
        try {
            FileBase64DTO base64PDF = fileManageService.convertBase64WordToPDF(file);
            return ResponseEntity.ok(base64PDF);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequest();
        }
    }

    @PostMapping(value = "/generate-pdf-url")
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
    	String fileName = null;
    	try {
    		fileName = urlService.getDataFromCode(encrypt);
		} catch (ExpiredUrlException e) {
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
}
