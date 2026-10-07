package com.github.zulisses.res_with_spring_boot.controllers.docs;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.zulisses.res_with_spring_boot.data.dto.v1.PersonDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

public interface PersonControllerDocs {

    @Operation(summary = "Finds a person", description = "Finds a person by its ID", tags = {"People"}, responses = {
        @ApiResponse(description = "Success", responseCode = "200", content = 
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PersonDTO.class))),
        @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
        @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
        @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
        @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
        @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    PersonDTO findById(Long id);

    @Operation(summary = "Finds all people", description = "Finds all people", tags = {"People"}, responses = {
        @ApiResponse(description = "Success", responseCode = "200", content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = PersonDTO.class)))
        }),
        @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
        @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
        @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
        @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    ResponseEntity<PagedModel<EntityModel<PersonDTO>>> findAll(
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "12") int size,
        @RequestParam(value = "direction", defaultValue = "asc") String direction
    );

    @Operation(summary = "Adds a new person", description = "Adds a new person", tags = {"People"}, responses = {
        @ApiResponse(description = "Success", responseCode = "200", content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PersonDTO.class))
        }),
        @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
        @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
        @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    PersonDTO create(PersonDTO person);

    @Operation(summary = "Updates a person", description = "Updates a person", tags = {"People"}, responses = {
        @ApiResponse(description = "Success", responseCode = "200", content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PersonDTO.class))
        }),
        @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
        @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
        @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
        @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    PersonDTO update(PersonDTO person);

    @Operation(summary = "Disables a person", description = "Disables a person by its ID", tags = {"People"}, responses = {
        @ApiResponse(description = "Success", responseCode = "200", content = 
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PersonDTO.class))),
        @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
        @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
        @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
        @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
        @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    PersonDTO disablePerson(Long id);

    @Operation(summary = "Deletes a person", description = "Deletes a person by its ID", tags = {"People"}, responses = {
        @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
        @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
        @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
        @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
        @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    ResponseEntity<?> delete(Long id);

}