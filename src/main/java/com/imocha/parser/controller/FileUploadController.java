package com.imocha.parser.controller;

import com.imocha.parser.service.FileProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/upload")
public class FileUploadController {
    private final FileProcessingService fileProcessingService;

    @PostMapping("/zip")
    public ResponseEntity<?> uploadZip(@RequestParam MultipartFile file)
            throws IOException {

        fileProcessingService.processZip(file);
        return ResponseEntity.ok("Files processed successfully");
    }
}
