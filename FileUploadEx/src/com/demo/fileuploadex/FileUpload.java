package com.demo.fileuploadex;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

public class FileUpload {
	public static final String BOUNDARY = "*****";
	
	public static final String LINE_START = "--";
	public static final String LINE_END = "\r\n";
	
	private FileUploadConfig fileUploadConfig = null;
	private byte []fileData = null;
	private String mimeType = null;
	private String fieldName = "";
	
	private String paramName = "";
	private String paramValue = "";
	
    public FileUpload(FileUploadConfig fileUploadConfig) {
        	this.fileUploadConfig = fileUploadConfig;
    }
    
    public void attachFile(byte []fileData,String fieldName,String mimeType) {
    	this.fileData = fileData;
    	this.fieldName = fieldName;
    	this.mimeType = mimeType;
    }
    
    public void addParam(String name,String value) {
        this.paramName = name;
        this.paramValue = value;
    }
   
    public String startUploadingFile(String fileName) {
    	StringBuffer response = new StringBuffer();
    	if (this.fileUploadConfig != null) {
    		HttpURLConnection connection = null;
    		DataOutputStream dataOutput = null;
    		try {
    			URL url = new URL(this.fileUploadConfig.getFileUploadHost());
    			if (!url.getProtocol().toLowerCase().equals("https")) {
    				connection = (HttpURLConnection) url.openConnection();
    				if (connection != null) {
    				    connection.setDoInput(true);
    				    connection.setDoOutput(true);
    				    connection.setUseCaches(false);
    				    
    				    connection.setRequestMethod("POST");
    				    connection.setRequestProperty("ENCTYPE","multipart/form-data");
    				    connection.setRequestProperty("Connection","Keep-Alive");
    				    connection.setRequestProperty("Content-Type","multipart/form-data;boundary=" + FileUpload.BOUNDARY);
    				
    				    String extraParams = "";
    				    
    				    // add extra params
    				    if (this.paramName != null && this.paramValue != null) {
    				        extraParams+= FileUpload.LINE_START + FileUpload.BOUNDARY + FileUpload.LINE_END;
    				        extraParams+= "Content-Disposition: form-data; name=\"" + this.paramName + "\";";
    				        extraParams+= FileUpload.LINE_END + FileUpload.LINE_END;
    				        extraParams+= this.paramValue;
    				        extraParams+= FileUpload.LINE_END;
    				    }
    				    
    				    // add File body
    				    extraParams+= FileUpload.LINE_START + FileUpload.BOUNDARY + FileUpload.LINE_END;
    				    extraParams+= "Content-Disposition: form-data; name=\"" + this.fieldName + "\"; filename=\"";
    				    byte []extraBytes = extraParams.getBytes("UTF-8");
    				    
    				    String midParams = "\"" + FileUpload.LINE_END + "Content-Type: " + this.mimeType + FileUpload.LINE_END + FileUpload.LINE_END;
    				    String tailParams = FileUpload.LINE_END + FileUpload.LINE_START + FileUpload.BOUNDARY + FileUpload.LINE_START + FileUpload.LINE_END;
    				    
    				    byte []fileNameBytes = fileName.getBytes("UTF-8");
    				    
    				    connection.setFixedLengthStreamingMode((int)(fileData.length + extraBytes.length + midParams.length() + tailParams.length() + fileNameBytes.length));
    				    
    				    Log.d("FileUpload",extraParams + fileName + midParams + "FILEDATA" + tailParams);
    				    
    				    dataOutput = new DataOutputStream(connection.getOutputStream());
				    	dataOutput.write(extraBytes);
				    	dataOutput.write(fileNameBytes);
				    	dataOutput.writeBytes(midParams);
				    	dataOutput.write(fileData,0,fileData.length);
				    	dataOutput.writeBytes(tailParams);
				    	
				    	dataOutput.flush();
				    	dataOutput.close();
				    	  				    	
				        DataInputStream dataInput = new DataInputStream(connection.getInputStream());
				        if (dataInput != null) {
				            String dataLine = "";
				            while ((dataLine = dataInput.readLine()) != null) {
				            	response.append(dataLine);
				            }
				            dataInput.close();
				        }
    				} else {
    					throw new Exception("Trouble creating http connection!");
    				}
    			} else {
    				throw new Exception("HTTPS Protocol currently not supported!");
    			}
    		} catch (Exception exception) {
    			response.append(exception.getLocalizedMessage());
    		} finally {
    			if (connection != null) {
    				connection.disconnect();
    			}
    		}
    	}
    	return response.toString();
    }
    
    public void dispose() {
    	this.fileData = null;
    }
}
