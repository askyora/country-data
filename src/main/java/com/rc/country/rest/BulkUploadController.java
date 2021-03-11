package com.rc.country.rest;

import com.rc.country.entity.BaseObject;
import com.rc.country.service.AbstractDataLoader;
import com.rc.country.service.FileLoaderFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/file")
public class BulkUploadController {

    @Autowired
    private FileLoaderFactory factory;

    @Operation(summary = "Data Loader")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully Uploaded.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid Data.", content = @Content),
            @ApiResponse(responseCode = "409", description = "Data Conflict.", content = @Content),
            @ApiResponse(responseCode = "501", description = "Not Implemented.", content = @Content)
    })
    @PostMapping(value = "/{name}/upload")
    public ResponseEntity<List<BaseObject>> uploadFile(@RequestParam("file") MultipartFile file,
                                                       @PathVariable(value = "name", required = true) String name) throws IOException {
        Optional<AbstractDataLoader> loader = factory.getLoaderByName(name);

        if (loader.isPresent()) {
            return ResponseEntity.ok(loader.get().load(file.getInputStream()));
        }
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}