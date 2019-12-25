package com.ge.springbatch.readmultifiles;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.ge.springbatch.readmultifiles.model.Employee;

@Component
public class NewJob implements StepExecutionListener{
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Value("input/inputData*.csv")
	Resource[] inputResources;

	@Bean
	public Job readCSVFilesJob() {
		return jobBuilderFactory
			   .get("readCSVFilesJob")
			   .incrementer(new RunIdIncrementer())
			   .start(step1())
			   .build();
}
	
	@Bean
	public Job readCSVFilesTaskletJob() {
		return jobBuilderFactory
			   .get("readCSVFilesTaskletJob")
			   .incrementer(new RunIdIncrementer())
			   .start(step2())
			   .build();
}
	
	@Bean
    public Step step2() {
		return stepBuilderFactory.get("Tasklet step2")
        		.tasklet(new CustomReader())
                .build();
    }
	
	@Bean
    public Step step1() {
		return stepBuilderFactory.get("step1")
        		.<Employee, Employee>chunk(5)
                .reader(reader())
                .writer(writer())
                .faultTolerant()
                .skipLimit(1000)
                .skipPolicy(new NewSkipPolicy(stepExecution))
                .listener(newSkipListener)
                .build();
    }
	
	private StepExecution stepExecution;
	
/*	@Autowired
	private NewSkipPolicy skipPolicy;*/
	
	@Autowired
	private NewSkipListener newSkipListener;
	

	
	@Bean
    public MultiResourceItemReader<Employee> multiResourceItemReader() 
    {
        MultiResourceItemReader<Employee> resourceItemReader = new MultiResourceItemReader<Employee>();
		resourceItemReader.setResources(inputResources);
        resourceItemReader.setDelegate(reader());
        return resourceItemReader;
    }
 
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Bean
    @StepScope
    public FlatFileItemReader<Employee> reader() 
    {
    
    	Resource[] resources = new Resource[0];
    	 //Create reader instance
        FlatFileItemReader<Employee> reader = new FlatFileItemReader<Employee>();
    	try{
    	resources = inputResources;
    	
        //Set number of lines to skips. Use it if file has header rows.
        reader.setLinesToSkip(1);   
         
        //Configure how each line will be parsed and mapped to different values
        reader.setLineMapper(new DefaultLineMapper() {
            {
                //3 columns in each row
                setLineTokenizer(new DelimitedLineTokenizer() {
                    {
                        setNames(new String[] { "id", "firstName", "lastName" });
                    }
                });
                //Set values in Employee class
                setFieldSetMapper(new BeanWrapperFieldSetMapper<Employee>() {
                    {
                        setTargetType(Employee.class);
                    }
                });
            }
        });
        reader.setResource(resources[0]);
    	}catch(Exception e){
        stepExecution.getExecutionContext().put("fileName", resources[0].getFilename());
    	}
        return reader;
    }
    
    @Bean
    public ConsoleItemWriter<Employee> writer() 
    {
        return new ConsoleItemWriter<Employee>();
    }

	@Override
	public void beforeStep(StepExecution stepExecution) {
		this.stepExecution = stepExecution;
		
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		// TODO Auto-generated method stub
		return null;
	}
}