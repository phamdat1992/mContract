package vn.inspiron.mcontract.modules.Contract.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.inspiron.mcontract.modules.Entity.FileEntity;
import vn.inspiron.mcontract.modules.Exceptions.BadRequest;
import vn.inspiron.mcontract.modules.Repository.FilesRepository;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String UPLOAD_DIR;

    @Autowired
    private FilesRepository filesRepository;

    public Long storeFile(MultipartFile file, Long userId) {

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

        return fileEntity.getId();
    }

    public Resource loadFile(Long fileId) throws Exception {

        Optional<FileEntity> mayBeFileEntity = filesRepository.findById(fileId);

        if (mayBeFileEntity.isEmpty()) {
            throw new FileNotFoundException();
        }

        FileEntity fileEntity = mayBeFileEntity.get();

        String fullPath = fileEntity.getUploadPath();

        try {
            Path filePath = Paths.get(fullPath).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException();
            }
        } catch (MalformedURLException e) {
            throw new FileNotFoundException();
        }
    }
}
