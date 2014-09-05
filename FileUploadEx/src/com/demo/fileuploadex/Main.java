package com.demo.fileuploadex;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class Main extends ActionBarActivity {
	private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        textView = (TextView)findViewById(R.id.debug_output);
        
        FileUploadThread fileUploadThread = new FileUploadThread();
        fileUploadThread.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private byte []getImageData(String fileName) {
    	InputStream fileInput = null;
    	byte []dataBuffer = null;
    	ByteArrayOutputStream dataOutput = new ByteArrayOutputStream();;
    	try {
    	    fileInput = this.getAssets().open(fileName);
    	    if (fileInput != null) {
    		    dataBuffer = new byte[fileInput.available()];
    		    fileInput.read(dataBuffer);
    		    dataOutput.write(dataBuffer);
    	    }
    	} catch (Exception exception) {
    		exception.printStackTrace();
    	}
    	return dataOutput.toByteArray();
    }
    
    class FileUploadThread extends Thread {
    	String response = "No response";
    	public void run() {
            FileUpload fileUpload = new FileUpload(new FileUploadConfig("http://192.168.50.37/demo/upload"));
            if (fileUpload != null) {
            	// applicant_photo
            	// applicant_id
            	
            	fileUpload.attachFile(getImageData("mark-zuckerberg-5.jpg"),"file","image/jpeg");
            	fileUpload.addParam("id","1");
            	response = fileUpload.startUploadingFile("mark-zuckerberg-5.jpg");
            	fileUpload.dispose();
            	
            	if (response != null) {
            		Main.this.runOnUiThread(new Runnable() {
            			public void run() {
            				textView.setText(response);
            			}
            		});
            	}
            }
    	}
    }
}
