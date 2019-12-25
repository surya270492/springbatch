package com.ge.springbatch.readmultifiles;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.file.FlatFileParseException;

public class NewSkipPolicy implements SkipPolicy{

	private StepExecution stepExecution;
	
	    public NewSkipPolicy(StepExecution stepExecution) {
	        this.stepExecution = stepExecution;
	    }

	@Override
	public boolean shouldSkip(Throwable t, int skipCount) throws SkipLimitExceededException {
		// TODO Auto-generated method stub
		if(t instanceof FlatFileParseException){
			FlatFileParseException e = (FlatFileParseException)t;
			System.out.println(""+e.getInput());
		System.out.println(""+stepExecution.getExecutionContext().get("fileName"));
		return true;
		}
		return false;
	}

}
