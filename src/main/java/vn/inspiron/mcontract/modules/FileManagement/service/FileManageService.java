package vn.inspiron.mcontract.modules.FileManagement.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.inspiron.mcontract.modules.Entity.FileEntity;
import vn.inspiron.mcontract.modules.Exceptions.BadRequest;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileManageService {

    @Value("${file.upload-dir}")
    private String UPLOAD_DIR;

    public Long storeFile(MultipartFile file, long userId) {
        String targetFilePath = null;
        try {
            targetFilePath = saveFileToStorage(file);
        } catch (Exception e) {
            throw new BadRequest();
        }

//        FileEntity fileEntity = new FileEntity();
//        fileEntity.setContentType(file.getContentType());
//        fileEntity.setOriginalFilename(file.getOriginalFilename());
//        fileEntity.setUploadPath(targetFilePath);
//        fileEntity.setUploadedBy(userId);

        return userId;
    }

    private String saveFileToStorage(MultipartFile file) throws Exception {
        String originalFilename = file.getOriginalFilename();

        String extension = "";
        try {
            extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        } catch (Exception e) {
            extension = "";
        }

        String newFilename = UUID.randomUUID().toString();

        if (!extension.equals("")) {
            newFilename += "." + extension;
        }

        Path targetPath = Paths.get(UPLOAD_DIR);
        Path targetFile = targetPath.toAbsolutePath().normalize().resolve(newFilename);

        if (!Files.exists(targetPath)) {
            Files.createDirectories(targetPath);
        }
        Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);
//        Files.copy(file.getInputStream(), targetFile, StandardCopyOption.ATOMIC_MOVE);

        return targetFile.toString();
    }
}
