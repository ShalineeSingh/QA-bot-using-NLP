package com.example.demo;

import org.springframework.stereotype.Service;

/**
 * Created by shalineesingh on 21/12/17.
 */

@Service
public class Utils {
    public String getFilesList() {
        return "['File1', 'File2']";
    }
}
