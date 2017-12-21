package com.example.demo;
/**
 * Created by shalineesingh on 19/12/17.
 */

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;


@Service
public class pdfParser {
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

    public Boolean getText() {
        try {
            BodyContentHandler handler = new BodyContentHandler(-1);
            Metadata metadata = new Metadata();
            String dirName = System.getProperty("user.dir");

            Utils.saveFileFromUrlWithJavaIO(
                    dirName +  "/" + filename + ".pdf",
                    filepath);


            FileInputStream inputstream = new FileInputStream(new File(dirName + "/" + filename + ".pdf"));
            ParseContext pcontext = new ParseContext();

            //parsing the document using PDF parser
            PDFParser pdfparser = new PDFParser();
            pdfparser.parse(inputstream, handler, metadata, pcontext);
            PrintWriter out = new PrintWriter(dirName + "/docs/" + filename + ".txt");
            out.write(handler.toString());
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    private String filename;
    private String filepath;

}