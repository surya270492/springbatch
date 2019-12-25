package com.ge.springbatch.readmultifiles;

import org.springframework.batch.core.annotation.OnSkipInProcess;
import org.springframework.batch.core.annotation.OnSkipInRead;
import org.springframework.batch.core.annotation.OnSkipInWrite;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.stereotype.Component;

@Component
public class NewSkipListener {

	@OnSkipInRead
    public void onSkipInRead(Throwable t) {
        System.out.println("From onSkipInRead -> " + t.getMessage());
        if(t instanceof FlatFileParseException)
        {
        	FlatFileParseException ffpe = (FlatFileParseException)t;
        	System.out.println("From onSkipInRead -> " + t.getMessage());
        	System.out.println("From onSkipInRead -> " + ffpe.getLineNumber());
        	System.out.println("From onSkipInRead -> " + ffpe.getInput());
        }
    }

    @OnSkipInWrite
    public void onSkipInWrite(String item, Throwable t) {
        System.out.println("From onSkipInWrite: " + item + " -> " + t.getMessage());
    }

    @OnSkipInProcess
    public void onSkipInProcess(String item, Throwable t) {
        System.out.println("From onSkipInProcess: " + " -> " + t.getMessage());
    }
}
