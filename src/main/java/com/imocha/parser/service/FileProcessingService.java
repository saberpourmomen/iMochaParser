package com.imocha.parser.service;

import com.imocha.parser.repository.CallRecordRepository;
import com.imocha.parser.util.FileNameValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
@RequiredArgsConstructor
public class FileProcessingService {
    private final CallRecordRepository callRecordRepository;
    private final JobLauncher jobLauncher;
    private final Job importCallJob;


    public void processZip(MultipartFile zipFile) throws IOException {

        try (ZipInputStream zis = new ZipInputStream(zipFile.getInputStream())) {

            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null) {

                if (!FileNameValidator.isValidate(entry)) {
                    continue;
                }

                Path tempFile = Files.createTempFile(
                        "call_", "_" + entry.getName()
                );

                try (OutputStream os = Files.newOutputStream(tempFile)) {
                    StreamUtils.copy(zis, os);
                }

                JobParameters jobParameters = new JobParametersBuilder()
                        .addString("filePath", tempFile.toAbsolutePath().toString())
                        .addString("fileName", entry.getName())
                        .addLong("timestamp", System.currentTimeMillis())
                        .toJobParameters();

                jobLauncher.run(importCallJob, jobParameters);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to process ZIP file", e);
        }
    }

}
