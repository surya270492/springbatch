package com.ge.springbatch.exception;

public class CustomFileReaderException extends Exception {

	private String fileName;

	public CustomFileReaderException(String fileName) {
		super();
		this.fileName = fileName;
	}
	
	public CustomFileReaderException(String fileName, Exception e) {
		super();
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}
