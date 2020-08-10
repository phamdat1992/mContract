package vn.inspiron.mcontract.modules.Contract.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vn.inspiron.mcontract.modules.Contract.services.FileStorageService;

@RestController
public class FileUploadController {
    @Autowired
    FileStorageService fileStorageService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             @RequestParam("user-id") Long userId) {
        String uploadedPath = fileStorageService.storeFile(file, userId);

        return ResponseEntity.ok(uploadedPath);
    }
}
