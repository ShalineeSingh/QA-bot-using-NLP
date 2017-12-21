package com.example.demo;

import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by shalineesingh on 21/12/17.
 */

@Service
public class TextParser {
    public String getFileName(){
     return this.filename;
    }
    public void setFileName(String filename){
        this.filename = filename;
    }
    public String getFilePath(){
        return this.filepath;
    }
    public void setFilePath(String filepath){
        this.filepath = filepath;
    }

    public void getFileText() throws IOException {
        //TODO: to read txt file and return txt
        String dirName = System.getProperty("user.dir");
        Utils.saveFileFromUrlWithJavaIO(
                dirName +  "/docs/" + filename + ".txt",
                filepath);
    }
    private String filename;
    private String filepath;
}
