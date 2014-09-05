package com.demo.fileuploadex;

public class FileUploadConfig {
    private String fileUploadHost = null;
    
    public FileUploadConfig(String fileUploadHost) {
    	this.setFileUploadHost(fileUploadHost);
    }
    
    public void setFileUploadHost(String fileUploadHost) {
        this.fileUploadHost = fileUploadHost;	
    }
    
    public String getFileUploadHost() {
    	return this.fileUploadHost;
    }
}
