package com.imocha.parser.service;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class TempFileCleanupListener
        extends JobExecutionListenerSupport {

    @Override
    public void afterJob(JobExecution jobExecution) {

        String path =
                jobExecution.getJobParameters()
                        .getString("filePath");

        if (path != null) {
            try {
                Files.deleteIfExists(Paths.get(path));
            } catch (IOException ignored) {}
        }
    }
}