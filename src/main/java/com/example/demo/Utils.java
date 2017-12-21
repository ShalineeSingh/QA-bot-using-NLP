package com.example.demo;

import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by shalineesingh on 21/12/17.
 */

@Service
public class Utils {
    public String[] getFilesList() {
        ArrayList<String> filePaths = new ArrayList<>();
        String dirName = System.getProperty("user.dir") + "/maps";
        File folder = new File(dirName);
        File[] files = folder.listFiles();
        for (File file : files){

                String fileName = file.getName();
                filePaths.add(fileName);
                System.out.println(file.getName());

        }
        return filePaths.toArray(new String[filePaths.size()]);
    }
    public static void saveFileFromUrlWithJavaIO(String fileName, String fileUrl)
            throws MalformedURLException, IOException {
        BufferedInputStream in = null;
        FileOutputStream fout = null;
        try {
            in = new BufferedInputStream(new URL(fileUrl).openStream());
            fout = new FileOutputStream(fileName);

            byte data[] = new byte[1024];
            int count;
            while ((count = in.read(data, 0, 1024)) != -1) {
                fout.write(data, 0, count);
            }
        } finally {
            if (in != null)
                in.close();
            if (fout != null)
                fout.close();
        }
    }

    public String getFileName() {
        return  this.filename;
    }
    public void setFileName(String filename) {
        this.filename = filename;
    }
    public static String filename;
}
