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

    private static String fileName ;
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
        return "Welcome to QnA";
    }



    @RequestMapping(value = "convertToTxt", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getFile(@RequestHeader("filePath") String filePath,@RequestHeader("fileName") String fileName, HttpServletResponse res) throws TikaException, IOException, SAXException {
        PdfParser.setFilePath(filePath);
        PdfParser.setFileName(fileName);
        utils.setFileName(fileName);
        System.out.println(fileName);

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
            data.put("text", "The document is uploaded Successfully and above are the relevant topics of the selected document. Feel free to ask question related to the topics :slightly_smiling_face:");
            response.put("data", data);
        } catch(Exception ex) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @RequestMapping(value = "getText", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getTxtFile(@RequestHeader("filePath") String filePath, @RequestHeader("fileName") String fileName,HttpServletResponse res) {
        textParser.setFilePath(filePath);
        textParser.setFileName(fileName);
        utils.setFileName(fileName);
        JSONObject response;
        System.out.println(fileName);
        try {
            textParser.getFileText();
            indexerParaDocs.constructIndex(fileName);
//            String text = textParser.getFileText();
            response = new JSONObject();
            JSONObject data = new JSONObject();
            data.put("type", "text");
            data.put("text", "The document is uploaded Successfully and above are the relevant topics of the selected document. Feel free to ask question related to the topics :slightly_smiling_face:");
            response.put("data", data);
            System.out.println(response);
        } catch(Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @RequestMapping(value = "getLocale", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getFile(@RequestParam("locale") String locale) {
        JSONObject response;
        if(locale.equals("en-US")){
            response = new JSONObject();
            JSONObject data = new JSONObject();
            data.put("ENGATI.DASHBOARD","DASHBOARD");
            data.put("ENGATI.CHANNELS","CHANNELS");
            data.put("ENGATI.SETTINGS","SETTINGS");
            data.put("ENGATI.SIGNIN","SIGNIN");
            data.put("ENGATI.FORGOT_PASSWORD","FORGOT PASSWORD");
            data.put("ENGATI.NEW_ACCOUNT","Want a new account");
            response.put("displayKeyValueMap",data);
        }else{
            response = new JSONObject();
            JSONObject data = new JSONObject();
            data.put("ENGATI.DASHBOARD","Tablero");
            data.put("ENGATI.CHANNELS","Canales");
            data.put("ENGATI.SETTINGS","Configuraciones");
            data.put("ENGATI.SIGNIN","Registrarse");
            data.put("ENGATI.FORGOT_PASSWORD","Se te olvidó tu contras");
            data.put("ENGATI.NEW_ACCOUNT","Desea nueva cuenta");
            response.put("displayKeyValueMap",data);
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
            String ans = "";
            for (String a : files){
                ans += a;
                ans = ans + "\n";
            }
            data.put("text",ans);
            data.put("text", ans);
            response.put("data", data);
        }catch(Exception ex){
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }
////TODO: findIndexer if index already exists : findIndexer();
//    @RequestMapping(value = "getIndexedFileTopics", method = RequestMethod.GET, produces = "application/json")
//    public ResponseEntity<?> getTrainedFileDetails(@RequestHeader("fileName") String fileName,HttpServletResponse res) {
//        JSONObject response;
//        try{
//            indexerParaDocs.findIndexer();
//            response = new JSONObject();
//            JSONObject data = new JSONObject();
//            data.put("type", "text");
//            data.put("text", "Getting relevant topics...");
//            response.put("data", data);
//        }catch(Exception ex){
//            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
//        }
//        return new ResponseEntity(response, HttpStatus.OK);
//    }

    //TODO: RankQueryTokens: call after user enters the query
    @RequestMapping(value = "getRelevantParas", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getRelevantParas(@RequestParam("question") String query,HttpServletResponse res) {
        JSONObject response;

        try{

            String[] rankedDocs = indexerParaDocs.RankQueryTokens(query);
            response = new JSONObject();
            JSONObject data = new JSONObject();
            data.put("type", "text");
            String ans = "";
            for (String a : rankedDocs){
                ans += a;
                ans = ans + "\n\n";
            }
            data.put("text",ans);
            response.put("data", data);
        }catch(Exception ex){
            ex.printStackTrace();
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

    //TODO : code if a document is selected, indexer should change + endpoint for this
    @RequestMapping(value = "selectDocument", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDoc(@RequestParam("fileName") String fileName,HttpServletResponse res) {
        JSONObject response;
        try{
//            String[] rankedDocs = indexerParaDocs.RankQueryTokens(query);
            indexerParaDocs.findIndexer(fileName);
            response = new JSONObject();
            JSONObject data = new JSONObject();
            data.put("type", "text");
            data.put("text","getting relevant topics for this file .. ");
            response.put("data", data);
        }catch(Exception ex){
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

}

