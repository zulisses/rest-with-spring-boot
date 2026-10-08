package com.github.zulisses.res_with_spring_boot.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.github.zulisses.res_with_spring_boot.config.FileStorageConfig;
import com.github.zulisses.res_with_spring_boot.exception.FileNotFoundException;
import com.github.zulisses.res_with_spring_boot.exception.FileStorageException;

@Service
public class FileStorageService {

    private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);

    private final Path fileStorageLocation;

    public FileStorageService(FileStorageConfig fileStorageConfig) {
        Path path = Paths.get(fileStorageConfig.getUploadDir()).toAbsolutePath().normalize();
        this.fileStorageLocation = path;

        try {
            logger.info("Creating directory: " + path.toAbsolutePath());
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception e) {
            logger.error("Could not create the directory where the uploaded files will be stored: " + path.toAbsolutePath(), e);
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored: " + path.toAbsolutePath(), e);
        }
    }

    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if(fileName.contains("..")) {
                logger.error("Sorry! Filename contains invalid path sequence " + fileName);
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            logger.info("Storing file: " + fileName);
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (Exception e) {
            logger.error("Could not store file " + fileName + ". Please try again!", e);
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", e);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            }
            else {
                logger.error("File not found: " + fileName);
                throw new FileNotFoundException("File not found: " + fileName);
            }
        } catch (Exception e) {
            logger.error("File not found: " + fileName);
            throw new FileNotFoundException("File not found: " + fileName, e);
        }
    }
}
