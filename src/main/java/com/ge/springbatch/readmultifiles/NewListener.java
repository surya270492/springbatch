package com.ge.springbatch.readmultifiles;

import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.stereotype.Component;

import com.ge.springbatch.readmultifiles.model.Employee;

@Component
public class NewListener implements ItemReadListener<Employee> {

	@Override
	public void afterRead(Employee arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeRead() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReadError(Exception t) {
		// TODO Auto-generated method stub
		if(t instanceof FlatFileParseException){
	    System.out.println("Error");
		}
	}

}
