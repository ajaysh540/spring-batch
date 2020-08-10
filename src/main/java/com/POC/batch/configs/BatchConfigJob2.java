package com.POC.batch.configs;

import java.io.IOException;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.POC.batch.model.DataEntity;
import com.POC.batch.repository.DataEntityRepo;

@Configuration
public class BatchConfigJob2 {
	@Autowired
	StepBuilderFactory stepBuilder;
	@Autowired
	JobBuilderFactory jobBuilder;
	@Autowired
	DataEntityRepo dataEntityRepo;
	
	
	@Bean
	public Step step2(ItemReader<DataEntity> reader2,
			ItemProcessor<DataEntity, DataEntity> processor2,ItemWriter<DataEntity> writer2) {
		return stepBuilder.get("step2").<DataEntity,DataEntity>chunk(1000)
				.reader(reader2).processor(processor2)
				.writer(writer2).build();
	}
	
	@Bean
	public Job job2(Step step2,JobExecutionListener listener) {
		return jobBuilder.get("job2").incrementer(new RunIdIncrementer())
				.listener(listener).start(step2).build();
	}
	
	@Bean
	public ItemReader<DataEntity> reader2(DataSource dataSource){
		return new JdbcCursorItemReaderBuilder<DataEntity>().name("jdbc_reader").dataSource(dataSource)
		.fetchSize(1000).sql("Select * from person").beanRowMapper(DataEntity.class).build();
	}
	
	@Bean
	public ItemProcessor<DataEntity, DataEntity> processor2(){
		
		return (p)->{
			p.setString1(p.getString1().toUpperCase());
			p.setString2(p.getString2().toUpperCase());
			p.setString3(p.getString3().toUpperCase());
			p.setString4(p.getString4().toUpperCase());
			return p;
		};
	}
	
	@Bean
	public ItemWriter<DataEntity> writer2() throws IOException{

		return new FlatFileItemWriterBuilder<DataEntity>().name("csv_writer").delimited().names("id","string1","string2","string3","string4")
				.resource(new ClassPathResource("Output.csv")).build();
	}
	
}
