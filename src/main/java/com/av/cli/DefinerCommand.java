package com.av.cli;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@Slf4j
@RequiredArgsConstructor
public class DefinerCommand {

    private final JobLauncher jobLauncher;
    private final Job importJob;

    @ShellMethod(value = "startMigrationJob", key = "sm-jl")
    public void startMigrationJobWithJobLauncher() throws Exception {
        JobExecution execution = jobLauncher.run(importJob, new JobParametersBuilder().toJobParameters());
        System.out.println(execution);
    }

}
