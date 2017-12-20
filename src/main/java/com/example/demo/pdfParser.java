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
    public String getFile(){
        return this.filename;
    }

    public void setFile(String filename){
        this.filename = filename;
    }

    public String getText() throws IOException,TikaException, SAXException{
        BodyContentHandler handler = new BodyContentHandler(-1);
        Metadata metadata = new Metadata();
        String dirName = System.getProperty("user.dir");
        saveFileFromUrlWithJavaIO(
                dirName + "/sample.pdf",
                filename);


        FileInputStream inputstream = new FileInputStream(new File(dirName + "/sample.pdf"));
        ParseContext pcontext = new ParseContext();

        //parsing the document using PDF parser
        PDFParser pdfparser = new PDFParser();
        pdfparser.parse(inputstream, handler, metadata,pcontext);
        PrintWriter out = new PrintWriter(dirName  + "/output.txt");
        out.write(handler.toString());
        out.flush();
        out.close();
        return handler.toString();
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
    private String filename;

}