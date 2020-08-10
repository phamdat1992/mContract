package vn.inspiron.mcontract.modules.Contract.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.inspiron.mcontract.modules.Entity.FileEntity;
import vn.inspiron.mcontract.modules.Exceptions.BadRequest;
import vn.inspiron.mcontract.modules.Repository.FilesRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String UPLOAD_DIR;

    @Autowired
    private FilesRepository filesRepository;

    public String storeFile(MultipartFile file, Long userId) {

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

        try {
            if (!Files.exists(targetPath)) {
                Files.createDirectories(targetPath);
            }
            Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequest();
        }

        FileEntity fileEntity = new FileEntity();
        fileEntity.setContentType(file.getContentType());
        fileEntity.setOriginalFilename(originalFilename);
        fileEntity.setUploadPath(targetFile.toString());
        fileEntity.setUploadedBy(userId);

        filesRepository.save(fileEntity);

        return targetFile.toString();

    }

}
