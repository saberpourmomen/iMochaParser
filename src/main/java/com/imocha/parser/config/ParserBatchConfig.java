package com.imocha.parser.config;

import com.imocha.parser.model.CallRecord;
import com.imocha.parser.util.CallRecordParser;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class ParserBatchConfig {

    private final EntityManagerFactory emf;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    public FlatFileItemReader<String> reader() {
        FlatFileItemReader<String> reader = new FlatFileItemReader<>();
        reader.setLineMapper((line, lineNumber) -> line);
        return reader;
    }

    @Bean
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
    public Step step() {
        return new StepBuilder("callImportStep", jobRepository).<String, CallRecord>chunk(10, transactionManager).reader(reader()).processor(processor()).writer(writer()).build();
    }

    @Bean
    public Job importCallJob() {
        return new JobBuilder("callImportJob", jobRepository).start(step()).build();
    }
}