package com.github.zulisses.res_with_spring_boot.controllers.docs;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import com.github.zulisses.res_with_spring_boot.data.dto.v1.request.EmailRequestDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

public interface EmailControllerDocs {

    @Operation(summary = "Sends an email", description = "Sends an email", tags = {"Email"}, responses = {
        @ApiResponse(description = "Success", responseCode = "200", content = @Content),
        @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
        @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
        @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    ResponseEntity<String> sendEmail(@RequestBody EmailRequestDTO emailRequestDTO);

    @Operation(summary = "Sends an email with attachment", description = "Sends an email with attachment", tags = {"Email"}, responses = {
        @ApiResponse(description = "Success", responseCode = "200", content = @Content),
        @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
        @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
        @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    ResponseEntity<String> sendEmailWithAttachment(String emailRequestJson, MultipartFile multipartFile);

}
