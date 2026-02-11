package com.imocha.parser.service;

import com.imocha.parser.exception.JobExecutionException;
import com.imocha.parser.repository.CallRecordRepository;
import com.imocha.parser.util.FileNameValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
@RequiredArgsConstructor
@Slf4j
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

                String fileName = Paths.get(entry.getName())
                        .getFileName()
                        .toString();
                log.info("{} Start Processing File: {} {}",
                        "*".repeat(8), fileName, "*".repeat(8));
                Path tempFile = Files.createTempFile("call_", "_" + fileName);

                try (OutputStream os = Files.newOutputStream(tempFile)) {
                    StreamUtils.copy(zis, os);
                }

                JobParameters jobParameters = new JobParametersBuilder()
                        .addString("filePath", tempFile.toAbsolutePath().toString())
                        .addString("fileName", fileName)
                        .addLong("timestamp", System.currentTimeMillis())
                        .toJobParameters();

                launchJob(jobParameters, fileName);
                log.info("{} processing done for: {} {}",
                        "*".repeat(8), fileName, "*".repeat(8));
            }

        }
    }

    private void launchJob(JobParameters jobParameters, String fileName) {

        try {
            jobLauncher.run(importCallJob, jobParameters);

        } catch (JobExecutionAlreadyRunningException |
                 JobRestartException |
                 JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {

            log.error("Batch job failed for file: {}", fileName, e);

            throw new JobExecutionException(
                    "Batch execution failed for file: " + fileName,
                    e
            );
        }
    }

}
