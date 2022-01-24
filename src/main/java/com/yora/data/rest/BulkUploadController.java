package com.yora.data.rest;

import com.yora.data.entity.BaseObject;
import com.yora.data.service.AbstractDataLoader;
import com.yora.data.service.FileLoaderFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    protected Logger log = LogManager.getLogger(this.getClass());

    @Autowired
    private FileLoaderFactory factory;

    @PostMapping(value = "/{name}/upload")
    public ResponseEntity<List<BaseObject>> uploadFile(@RequestParam("file") MultipartFile file, @PathVariable(value = "name", required = true) String name) throws IOException {
        Optional<AbstractDataLoader> loader = factory.getLoaderByName(name);
        if (loader.isPresent()) {
            return ResponseEntity.ok(loader.get().load(file.getInputStream()));
        }
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}