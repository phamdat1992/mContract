package vn.inspiron.mcontract.modules.FileStorage.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vn.inspiron.mcontract.modules.FileStorage.services.FileStorageService;

@RestController
public class FileUploadController {

    private static final String UPLOAD_DIR = "upload";

    @Autowired
    FileStorageService fileStorageService;

    @PostMapping("/upload")
    // TODO: Retrieve userId from jwt
    public ResponseEntity<Long> uploadFile(@RequestParam("file") MultipartFile file,
                                             @RequestParam("user-id") Long userId) {
        Long fileId = fileStorageService.storeFile(file, userId, UPLOAD_DIR);

        return ResponseEntity.ok(fileId);
    }
}