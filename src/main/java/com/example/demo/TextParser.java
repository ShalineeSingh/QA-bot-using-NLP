package com.example.demo;

import org.springframework.stereotype.Service;

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

    public String getFileText(){
        //TODO: to read txt file and return txt

        return null;
    }
    private String filename;
}
