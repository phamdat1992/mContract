package vn.inspiron.mcontract.modules.FileStorage.services;

import eu.europa.esig.dss.model.InMemoryDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.inspiron.mcontract.modules.Common.util.Util;
import vn.inspiron.mcontract.modules.Entity.FileEntity;
import vn.inspiron.mcontract.modules.Exceptions.BadRequest;
import vn.inspiron.mcontract.modules.Repository.FilesRepository;

import java.io.File;
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
    private String ROOT_DIR;

    @Autowired
    private FilesRepository filesRepository;

    public Long storeFile(MultipartFile file, Long userId, String directory) {
        // Generate new filename
        String originalFilename = file.getOriginalFilename();
        String newFilename = originalFilename;

        Path relativePath = Paths.get(directory).resolve(newFilename);
        Path absolutePath = Paths.get(ROOT_DIR).toAbsolutePath().normalize().resolve(directory);
        Path absoluteFile = Paths.get(ROOT_DIR).toAbsolutePath().normalize().resolve(relativePath);

        // Copy data bytes
        try {
            if (!Files.exists(absolutePath)) {
                Files.createDirectories(absolutePath);
            }
            Files.copy(file.getInputStream(), absoluteFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequest();
        }

        // Save in database
        FileEntity fileEntity = new FileEntity();
        /*fileEntity.setContentType(file.getContentType());
        fileEntity.setOriginalFilename(originalFilename);
        fileEntity.setUploadPath(relativePath.toString());
        fileEntity.setUploadedBy(userId);*/

        filesRepository.save(fileEntity);

        return fileEntity.getId();
    }

    public Long storeSignedDocument(InMemoryDocument document, Long userId, String directory) {

        String filename = document.getName();

        Path relativePath = Paths.get(directory).resolve(filename);
        Path absolutePath = Paths.get(ROOT_DIR).toAbsolutePath().normalize().resolve(directory);
        Path absoluteFile = Paths.get(ROOT_DIR).toAbsolutePath().normalize().resolve(relativePath);

        // Copy data bytes
        try {
            if (!Files.exists(absolutePath)) {
                Files.createDirectories(absolutePath);
            }
            Files.write(absoluteFile, document.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequest();
        }

        // Save in database
        FileEntity fileEntity = new FileEntity();
        /*fileEntity.setContentType("signed-document");
        fileEntity.setOriginalFilename(document.getName());
        fileEntity.setUploadPath(relativePath.toString());
        fileEntity.setUploadedBy(userId);*/

        filesRepository.save(fileEntity);
        return fileEntity.getId();
    }

    public FileEntity loadFileEntity(Long fileId) throws Exception {

        Optional<FileEntity> mayBeFileEntity = filesRepository.findById(fileId);

        if (mayBeFileEntity.isEmpty()) {
            throw new FileNotFoundException();
        }

        FileEntity fileEntity = mayBeFileEntity.get();
        return fileEntity;
    }

    public Resource loadFileResource(String relativePath) throws Exception {
        Path absolutePath = Paths.get(ROOT_DIR).resolve(relativePath);
        try {
            Resource resource = new UrlResource(absolutePath.toUri());
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
