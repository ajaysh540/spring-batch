package com.POC.batch.configs;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import com.POC.batch.model.DataEntity;
import com.POC.batch.repository.DataEntityRepo;

@Configuration
public class BatchConfigJob1 {
	
	@Autowired
	StepBuilderFactory stepBuilder;
	@Autowired
	JobBuilderFactory jobBuilder;
	@Autowired
	DataEntityRepo dataEntityRepo;
	
	@Bean
	public JobListenerConfig listener() {
		return new JobListenerConfig();
	}
	
	@Bean
	public Step step1(ItemReader<DataEntity> reader,
			ItemProcessor<DataEntity, DataEntity> processor,ItemWriter<DataEntity> writer) {
		return stepBuilder.get("step1").<DataEntity,DataEntity>chunk(1000)
				.reader(reader).processor(processor)
				.writer(writer).build();
	}
	
	@Bean
	public Job job1(Step step1) {
		return jobBuilder.get("job1").incrementer(new RunIdIncrementer())
				.listener(listener()).start(step1).build();
	}
	
	@Bean
	public ItemReader<DataEntity> reader(){
		FlatFileItemReader<DataEntity> fileItemReader = new FlatFileItemReader<>();
		fileItemReader.setResource(new ClassPathResource("MOCK_DATA2.csv"));
		fileItemReader.setLinesToSkip(1);
		DefaultLineMapper<DataEntity> lineMapper = new DefaultLineMapper<>();
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setNames("id","string1","string2","string3","string4");
		BeanWrapperFieldSetMapper<DataEntity> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(DataEntity.class);
		lineMapper.setFieldSetMapper(fieldSetMapper);
		lineMapper.setLineTokenizer(lineTokenizer);
		fileItemReader.setLineMapper(lineMapper);
		fileItemReader.setSaveState(true);
		return fileItemReader;
	}
	
	@Bean
	public ItemProcessor<DataEntity, DataEntity> processor(){
		
		return (p)->{
			p.setString1(p.getString1().toUpperCase());
			p.setString2(p.getString2().toUpperCase());
			p.setString3(p.getString3().toUpperCase());
			p.setString4(p.getString4().toUpperCase());
			return p;
		};
	}
	
	@Bean
	public ItemWriter<DataEntity> writer(DataSource dataSource){
		
		return (items)->{
			dataEntityRepo.saveAll(items);
		};
	}
	
	@Bean
	public JobLauncher customJobLauncher(JobRepository jobRepository) throws Exception {
		SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
		jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
		jobLauncher.setJobRepository(jobRepository);
		jobLauncher.afterPropertiesSet();
		return jobLauncher;
	}
}
