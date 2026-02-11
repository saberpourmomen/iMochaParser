package com.imocha.parser.config;

import com.imocha.parser.model.CallRecord;
import com.imocha.parser.service.TempFileCleanupListener;
import com.imocha.parser.util.CallRecordParser;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class ParserBatchConfig {

    private final EntityManagerFactory emf;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final TempFileCleanupListener cleanupListener;

    @Bean
    @StepScope
    public FlatFileItemReader<String> reader(
            @Value("#{jobParameters['filePath']}") String filePath) {

        FlatFileItemReader<String> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource(filePath));
        reader.setLineMapper((line, lineNumber) -> line);
        return reader;
    }

    @Bean
    @StepScope
    public ItemProcessor<String, CallRecord> processor() {
        return CallRecordParser::parseLine;
    }

    @Bean
    public JpaItemWriter<CallRecord> writer() {
        JpaItemWriter<CallRecord> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(emf);
        return writer;
    }

    @Bean
    public Step step(FlatFileItemReader<String> reader,
                     ItemProcessor<String, CallRecord> processor,
                     JpaItemWriter<CallRecord> writer) {

        return new StepBuilder("callImportStep", jobRepository)
                .<String, CallRecord>chunk(100, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Job importCallJob(Step step) {

        return new JobBuilder("callImportJob", jobRepository)
                .listener(cleanupListener)
                .start(step)
                .build();
    }
}