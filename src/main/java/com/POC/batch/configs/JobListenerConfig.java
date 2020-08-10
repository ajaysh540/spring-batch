package com.POC.batch.configs;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class JobListenerConfig implements JobExecutionListener {

	@Override
	public void beforeJob(JobExecution jobExecution) {
		System.out.println("Before Job");
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		System.out.println("After Job "+jobExecution.getStatus());
	}

}
