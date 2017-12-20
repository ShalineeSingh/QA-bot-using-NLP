package com.example.demo;

import org.apache.tika.exception.TikaException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Created by shalineesingh on 20/12/17.
 */
@RestController
public class PdfParserController {

    @Autowired
    private pdfParser PdfParser;

    @Autowired
    private TextParser textParser;

    @Autowired
    private Utils utils;

    @RequestMapping(value = "/")
    public String hello(){
        return "hello sab chutiya hai ";
    }

    @RequestMapping(value = "/hello")
    public String helloAll(){
        return "hello sab chutiya hai logo ";
    }


    @RequestMapping(value = "convertToTxt", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getFile(@RequestHeader("filePath") String filePath, HttpServletResponse res) throws TikaException, IOException, SAXException {
        PdfParser.setFile(filePath);
        JSONObject response;

        try {
            String text = PdfParser.getText();
            response = new JSONObject();
            JSONObject data = new JSONObject();
            data.put("type", "text");
            data.put("text", "Indexing the data...");
            response.put("data", data);
            System.out.println(response);
        } catch(Exception ex) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @RequestMapping(value = "getText", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getTxtFile(@RequestHeader("filePath") String filePath, HttpServletResponse res) {
        textParser.setFile(filePath);
        JSONObject response;
        try {
            String text = textParser.getFileText();
            response = new JSONObject();
            JSONObject data = new JSONObject();
            data.put("type", "text");
            data.put("text", "Indexing the data...");
            response.put("data", data);
            System.out.println(response);
        } catch(Exception ex) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @RequestMapping(value = "getTrainedDocs", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getTrainedDocuments(HttpServletResponse res) {
        JSONObject response;
        try{
            String files = utils.getFilesList();
            response = new JSONObject();
            JSONObject data = new JSONObject();
            data.put("type", "text");
            data.put("text", files);
            response.put("data", data);
        }catch(Exception ex){
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }
}
