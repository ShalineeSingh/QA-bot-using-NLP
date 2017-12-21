package com.example.demo;

import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by shalineesingh on 21/12/17.
 */

@Service
public class TextParser {
    public String getFile(){
     return this.filename;
    }
    public void setFile(String filename){
        this.filename = filename;
    }

    public void getFileText() throws IOException {
        //TODO: to read txt file and return txt
        String dirName = System.getProperty("user.dir");
        Utils.saveFileFromUrlWithJavaIO(
                dirName +  "/docs/" + filename + ".txt",
                filename);
    }
    private String filename;
}
