package com.example.demo;

import com.sun.org.apache.xpath.internal.operations.Bool;
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

    @Autowired
    private IndexerParaDocs indexerParaDocs;

    @RequestMapping(value = "/")
    public String hello(){
        return "hello sab chutiya hai ";
    }


    @RequestMapping(value = "convertToTxt", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getFile(@RequestHeader("filePath") String filePath,@RequestHeader("fileName") String fileName, HttpServletResponse res) throws TikaException, IOException, SAXException {
        PdfParser.setFilePath(filePath);
        PdfParser.setFileName(fileName);
        JSONObject response;

        try {
            Boolean fileCreated = PdfParser.getText();
            if(fileCreated) {
                indexerParaDocs.constructIndex(fileName);
            }else{
                System.out.println("file" + fileCreated);
                throw new Exception();
            }
            response = new JSONObject();
            JSONObject data = new JSONObject();
            data.put("type", "text");
            data.put("text", "Indexing the data...");
            response.put("data", data);
        } catch(Exception ex) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @RequestMapping(value = "getText", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getTxtFile(@RequestHeader("filePath") String filePath, @RequestHeader("fileName") String fileName,HttpServletResponse res) {
        textParser.setFile(filePath);
        JSONObject response;
        try {
            textParser.getFileText();
            indexerParaDocs.constructIndex(fileName);
//            String text = textParser.getFileText();
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
            String[] files = utils.getFilesList();
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
//TODO: findIndexer if index already exists : findIndexer();
    @RequestMapping(value = "getIndexedFileTopics", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getTrainedFileDetails(@RequestHeader("fileName") String fileName,HttpServletResponse res) {
        JSONObject response;
        try{
            indexerParaDocs.findIndexer();
            response = new JSONObject();
            JSONObject data = new JSONObject();
            data.put("type", "text");
            data.put("text", "Getting relevant topics...");
            response.put("data", data);
        }catch(Exception ex){
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

    //TODO: RankQueryTokens: call after user enters the query
    @RequestMapping(value = "getRelevantParas", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getRelevantParas(@RequestHeader("query") String query,HttpServletResponse res) {
        JSONObject response;
        try{
            String[] rankedDocs = indexerParaDocs.RankQueryTokens(query);
            response = new JSONObject();
            JSONObject data = new JSONObject();
            data.put("type", "text");
            data.put("text",rankedDocs);
            response.put("data", data);
        }catch(Exception ex){
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

    //TODO : code if a document is selected, indexer should change + endpoint for this
}

