package com.github.zulisses.res_with_spring_boot.controllers.docs;

import com.github.zulisses.res_with_spring_boot.data.UploadFileResponseDTO;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "File Endpoint", description = "File API")
public interface FileControllerDocs {

    UploadFileResponseDTO uploadFile(MultipartFile file);

    List<UploadFileResponseDTO> uploadMultipleFiles(MultipartFile[] files);

    ResponseEntity<Resource> downloadFile(String fileName, HttpServletRequest request);
}
