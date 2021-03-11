package com.rc.country.rest;

import com.rc.country.service.DataLoaderServiceFactory;
import io.micrometer.core.annotation.Timed;
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

@RestController
@RequestMapping("/file")
public class BulkUploadController {

    @Autowired
    private DataLoaderServiceFactory dataLoader;

    @Timed
    @Operation(summary = "Data Loader")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully Uploaded.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid Data.", content = @Content),
            @ApiResponse(responseCode = "409", description = "Data Conflict.", content = @Content)
    })
    @PostMapping(value = "/{type}/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
                                             @PathVariable(value = "type", required = true) String type) throws IOException {
        return new ResponseEntity<List<?>>(dataLoader.load(file.getInputStream(), type), HttpStatus.OK);
    }
}