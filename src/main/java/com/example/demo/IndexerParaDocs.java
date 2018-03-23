package com.example.demo;

/**
 * Created by shalineesingh on 21/12/17.
 */


import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;
import java.util.regex.Pattern;
import java.io.*;

import static com.example.demo.Index.getEntityTags;

@Service
public class IndexerParaDocs {

    public ArrayList<termfreq> doclist=new ArrayList<termfreq>();
    public TreeMap<String,ArrayList > frequencyData = new TreeMap<String, ArrayList>();;
    public static TreeMap<String,ArrayList > indexedDocs = new TreeMap<String, ArrayList>();
    public static TreeMap<String,ArrayList > indexedMyDocs = new TreeMap<String, ArrayList>();
    public static TreeMap<String,Double > tfidfScore = new TreeMap<String, Double>();
    public Stemmer stemObj=new Stemmer();
//    private String RESULT_FNAME = "result.txt";
//    private String STOPWORD_INDEX = "stop-word-list.txt";
    public String[] sampleToken;
    public File[] fileList;
    public Map<String,String> qustionTypes = getQuestionTypes();


    public Map<String,String> getQuestionTypes(){
        Map<String,String> qustionTypes = new HashMap<String,String>();
        qustionTypes.put("what","DESCRIPTIVE");
        qustionTypes.put("how","DESCRIPTIVE");
        qustionTypes.put("why","DESCRIPTIVE");
        qustionTypes.put("when","DESCRIPTIVE");
        qustionTypes.put("who","PERSON");
        qustionTypes.put("where","PLACE");
        return  qustionTypes;

    }

    public  void indexResearchDoc()throws IOException{
        String filePath = "resDocs/";
        File dir = new File(filePath);
        File[] flist=dir.listFiles();
        String fileMyPath = "Docs/";
        File Mydir = new File(fileMyPath);
        File[] myflist=dir.listFiles();
        createIndexFile(indexedDocs, doclist,flist, "researchIndex");
        createIndexFile(indexedMyDocs, doclist,myflist, "researchMyIndex");
    }

    public TreeMap<String,Double > calculateTfIdf(TreeMap<String,ArrayList > indexedDocs, TreeMap<String,ArrayList > indexedMyDocs, int fileSize){
        for(String word : indexedMyDocs.keySet( )){
            ArrayList<termfreq> obj1=frequencyData.get(word);
            double temp=obj1.get(0).term_freq *Math.log(fileSize / obj1.size());
            tfidfScore.put(word,temp);

        }
        return tfidfScore;
    }
    public IndexerParaDocs(){
        doclist=new ArrayList<termfreq>();
        frequencyData = new TreeMap<String, ArrayList>();
        stemObj=new Stemmer();
    }

    public File[] getFileList(String filename){
        System.out.println(filename);
        String filePath = System.getProperty("user.dir") + "/para/"+filename;
        File dir = new File(filePath);
        File[] flist=dir.listFiles();
        fileList=new File[flist.length];
        fileList=flist;
        return flist;
    }
    //TODO contruct index from the file provided by the user
    //If the file is not indexed
    public void constructIndex(String filename) throws IOException {
        createParaDocs("docs/" + filename + ".txt", filename);
        File[] flist = getFileList(filename);
        System.out.println(filename);
        createIndexFile(frequencyData, doclist,flist, filename);
    }

    //-----  	@Contains all the method for Indexing the Documents.
    //If the file is already indexed
    public void findIndexer(String filename) throws IOException, ClassNotFoundException{
//        Scanner keyboard = new Scanner(System.in);
//        System.out.println("Enter The path of the collecetion");
//        String filePath=keyboard.nextLine();
//        File dir = new File(filePath);
//
//        createParaDocs("docs/hfj.pdf.txt");
//        File[] flist=dir.listFiles();
//        fileList=new File[flist.length];
//        fileList=flist;
//
//        System.out.println("Enter 1 to Construct the Indexer and 2 if the Indexer is already constructed");
//        int option=keyboard.nextInt();
//        switch(option){
//            case 1 :
//                createIndexFile(frequencyData, doclist,flist);
//                break;
//            case 2 :
//                frequencyData=readIndexFile();
//                break;
//
//            default : createIndexFile(frequencyData,doclist,flist);
//
//        }

        frequencyData=readIndexFile(filename);



//        System.out.println("Enter 1 if you want to print the posting List, 2 to Skip");
//        int option2=keyboard.nextInt();
//        switch(option2){
//            case 1 :
//                postingList(frequencyData,doclist);
//                break;
//            case 2:
//                break;
//
//        }

//        Scanner keyboard1 = new Scanner(System.in);
//        System.out.println("Enter the Query Ex- शक लूट ");
//        String query=keyboard1.nextLine();
//        String token[]=query.split(" ");
//        //sort an array.
//
//        Arrays.sort(token);
//        sampleToken=new String[token.length];
//        sampleToken=token;
//        for(int i=0;i<token.length;i++){
//            System.out.println(token[i]);
//
//        }
//        //call the rank function based on tfidf
//        rank(frequencyData,doclist,token,flist);
    }

    //get ranking of the retrieved files
    public String[] RankQueryTokens(String query) throws IOException, ClassNotFoundException {
        String token[]=query.split(" ");
        Arrays.sort(token);
        for(int i =0 ; i < token.length; i++){
            token[i] = token[i].toLowerCase();
        }
        String queryType = "";
        for(int i =0 ; i < token.length; i++){
            if(qustionTypes.containsKey(token[i])){
                queryType = token[i];
                break;
            }
        }
        sampleToken=new String[token.length];
        sampleToken=token;
        //call the rank function based on tfidf

        //TODO : set filename
        String filename = Utils.filename;
        File[] flist = getFileList(filename);
        return rank(frequencyData,doclist,token,flist,queryType);
    }

//    public void createFile(String fileData, int index){
//        try {
//            String filename = "docs/"+String.valueOf(index)+".txt";
//            File file = new File(filename);
//            FileWriter fileWriter = new FileWriter(file);
//            fileWriter.write(fileData);
//            fileWriter.flush();
//            fileWriter.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


    public void createParaDocs(String filePath, String filename) throws IOException{
        System.out.println(filename + " in create docs");
        BufferedWriter bw = null;
        try
        {
            FileInputStream fileStream = new FileInputStream(filePath);
            InputStreamReader input = new InputStreamReader(fileStream);
            BufferedReader reader = new BufferedReader(input);
            int count = 1;
            String line;
            while((line = reader.readLine()) != null)
            {
                if(line.equals(""))
                {
                    count++;
                }else{
                    try {
                        // APPEND MODE SET HERE
                        //create a folder with "filename"
                        File theDir = new File("para/" + filename);
                        System.out.println(theDir.getParentFile().getAbsolutePath());
                        if (!theDir.exists()) {
                            System.out.println("creating directory: " + theDir.getName());
                            boolean result = false;
                            File parent = theDir.getParentFile();

                            try{
//                                parent.mkdirs();
                                theDir.mkdirs();
                                result = true;
                            }
                            catch(SecurityException se){
                                //handle it
                            }
                            if(result) {
                                System.out.println("DIR created");
                            }
                        }
                        String paraname = "para/"+filename+"/"+String.valueOf(count)+".txt";
                        bw = new BufferedWriter(new FileWriter(paraname, true));
                        bw.write(line);
                        bw.newLine();
                        bw.flush();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    } finally {                       // always close the file
                        if (bw != null) try {
                            bw.close();
                        } catch (IOException ioe2) {
                        }
                    } // end try/catch/finally
                }
            }

        }
        catch (FileNotFoundException e)
        {
            System.err.println(e.getMessage());
            System.exit(-1);
        }

    }


    //--------------------- @read the stop words
    public TreeMap<String,Integer > readStopWords(String stopWordsFilename)
    {
        TreeMap<String,Integer > stopWords = new TreeMap<String,Integer >();

        try
        {
            Scanner stopWordsFile = new Scanner(new File(stopWordsFilename));
            int numStopWords = stopWordsFile.nextInt();

            for (int i = 0; i < numStopWords; i++){
                stopWords.put( stopWordsFile.next(),1);
            }
            stopWordsFile.close();
        }
        catch (FileNotFoundException e)
        {
            System.err.println(e.getMessage());
            System.exit(-1);
        }

        return stopWords;
    }

    //------          @read Index file from Disk
    public TreeMap<String,ArrayList> readIndexFile(String filename) throws IOException{
        FileInputStream fis = new FileInputStream("maps/" + filename);
        ObjectInputStream ois = new ObjectInputStream(fis);
        @SuppressWarnings("unchecked")

        TreeMap<String,ArrayList > list1 = new TreeMap<String,ArrayList >();

        try {
            list1=(TreeMap<String,ArrayList >) ois.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        ois.close();
        return list1;
    }
    //------         @Create Index and Store it Into a disk
    public void createIndexFile(TreeMap<String,ArrayList > frequencyData,ArrayList<termfreq> doclist,File[] flist, String filename) throws FileNotFoundException, IOException{
        int fileSize=flist.length;

        TreeMap<String,Integer> stopWords= readStopWords("stop-word-list.txt");
        int length =Integer.toString(fileSize).length();
        length=length-1; //
        int dividend=fileSize/(int)Math.pow(10,length);
        int remainder=fileSize% (int)Math.pow(10,length);

        if(remainder>0){
            Index[] obj=new Index[dividend+1];
            Thread[] thread=new Thread[dividend+1];
            for(int i=0;i<dividend;i++){
                obj[i]= new Index( i*((int)Math.pow(10,length))+1,(i+1)*((int)Math.pow(10,length)),flist,stopWords,frequencyData,doclist );
                thread[i]=new Thread(obj[i]);
            }
            obj[dividend]= new Index( dividend*((int)Math.pow(10,length))+1,(dividend+1)*((int)Math.pow(10,length))+remainder,flist,stopWords,frequencyData,doclist);
            thread[dividend]=new Thread(obj[dividend]);
            //start the threads
            for(int i=0;i<dividend+1;i++){
                thread[i].start();
            }
            //join the threads
            for(int i=0;i<dividend+1;i++){
                try{
                    thread[i].join();
                } catch( InterruptedException e){
                    e.printStackTrace();
                }

            }
        }else{
            Index[] obj=new Index[dividend];
            Thread[] thread=new Thread[dividend];
            obj[0]= new Index( 1,1,flist,stopWords,frequencyData,doclist );
            thread[0]=new Thread(obj[0]);
            thread[0].start();
            try{
                thread[0].join();
            } catch( InterruptedException e){
                e.printStackTrace();
            }
        }
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("maps/"+ filename));
        oos.writeObject(frequencyData);
        oos.flush();
        oos.close();


    }

    //--              @Genearate the rank based on the query.
    public String[] rank( TreeMap<String,ArrayList > frequencyData,ArrayList<termfreq> doclist, String[] token,File[] flist, String queryType)throws IOException, ClassNotFoundException{
        //changed rank function. It will now return String array of 20 documents name.
        String[] rankedDocs = new String[5];
        int[] classifiedAnswers = new int[5];
        String[] classifiedAnsString= new String[5];

        int fileSize=flist.length;
        double[] rank=new double[fileSize];
        Arrays.fill(rank,0.0);
        //loop for number of Query Words
        for(int i=0;i<token.length;i++){
            //if query word is contained in Dictionary
            //calculate tf-idf score for all document conatining that word
            if (frequencyData.containsKey(token[i])){
                ArrayList<termfreq> obj1=frequencyData.get(token[i]);
                double wieghtDiv=0;
                double temp;
                for(int x=0;x<frequencyData.get(token[i]).size();x++){
                    temp=obj1.get(x).term_freq *Math.log(fileSize / obj1.size());
                    temp=temp*temp;//squaring
                    wieghtDiv=wieghtDiv+temp;
                }
                double divd=Math.sqrt(wieghtDiv);
                for(int x=0;x<frequencyData.get(token[i]).size();x++){
                    //wieght of the i,j
                    double weight=obj1.get(x).term_freq *Math.log(fileSize / obj1.size());
                    weight=weight/divd;
                    //Going to Do Document Length Normalization.
                    rank[obj1.get(x).doc_id]=rank[obj1.get(x).doc_id]+(weight);
                }
            }
        }
        int z=fileSize;
        // logic to sort the index of the array.
        Integer[] idx= new Integer[z];

        for(int i=0;i<fileSize;i++){
            idx[i]=i;
        }
        Arrays.sort(idx,new Comparator<Integer>(){
            public int compare(final Integer o1,final Integer o2){
                return Double.compare(rank[o1],rank[o2]);
            }
        });

        for(int i=z-1;i>=z-5;i--){

            try{
                BufferedReader reader = new BufferedReader(new FileReader("para/"+Utils.filename+"/"+flist[idx[i]].getName()));
                String paraFile="";
                String line;
                while ((line = reader.readLine()) != null)
                {
                    paraFile= paraFile+line;
                }
                reader.close();
//                System.out.println(queryType);
//                System.out.println(queryType.equals("where"));


//                if(queryType.equals("who")){
//                    Map<String, String> entityTags = getEntityTags(paraFile);
//                    for(String word : entityTags.keySet( )){
//                        if(entityTags.get(word).equals("PERSON")){
//                            classifiedAnswers[fileSize-i-1]++;
//                            classifiedAnsString[fileSize-i-1]=word;
//                        }
//                    }
//                }
//                if(queryType.equals("where")){
//                    System.out.print("inside where");
//                    Map<String, String> entityTags = getEntityTags(paraFile);
//                    for(String word : entityTags.keySet( )){
//                        if(entityTags.get(word).equals("LOCATION")){
//                            classifiedAnswers[fileSize-i-1]++;
//                            classifiedAnsString[fileSize-i-1]=word;
//                        }
//                    }
//                }

                rankedDocs[fileSize-i-1]=paraFile;

            }
            catch (Exception e)
            {
                System.err.format("Exception occurred trying to read '%s'.", "para/"+flist[idx[i]].getName());
                e.printStackTrace();
                return null;
            }
        }
//        if(queryType.equals("who")){
//            Integer[] idx1= new Integer[5];
//
//            for(int i=0;i<5;i++){
//                idx1[i]=i;
//            }
//            Arrays.sort(idx1,new Comparator<Integer>(){
//                public int compare(final Integer o1,final Integer o2){
//                    return Double.compare(classifiedAnswers[o1],classifiedAnswers[o2]);
//                }
//            });
//            String[] result= new String[1];
//            result[0] = classifiedAnsString[idx1[4]];
//            return result;
//        }
//        if(queryType.equals("where")){
//                            System.out.println("inside where");
//
//            Integer[] idx1= new Integer[5];
//
//            for(int i=0;i<5;i++){
//                idx1[i]=i;
//            }
//            Arrays.sort(idx1,new Comparator<Integer>(){
//                public int compare(final Integer o1,final Integer o2){
//                    return Double.compare(classifiedAnswers[o1],classifiedAnswers[o2]);
//                }
//            });
//            String[] result= new String[1];
//            result[0] = classifiedAnsString[idx1[4]];
//            System.out.println(result[0]);
//
//            return result;
//
//        }
        return rankedDocs;
    }
//
//    public void postingList( TreeMap<String,ArrayList > frequencyData,ArrayList<termfreq> doclist){
//        System.out.println("======================Posting List=======================");
//        System.out.println("Term                     [Doc num,term frequency]");
//        for(String word : frequencyData.keySet( )){
//            System.out.printf("%15s  ", word);
//            for(int x=0;x<frequencyData.get(word).size();x++){
//                ArrayList<termfreq> obj1=frequencyData.get(word);
//                if(obj1.get(x)!=null)
//                    System.out.printf("[ %d ,%d ]",obj1.get(x).doc_id,obj1.get(x).term_freq);
//            }
//            System.out.println();
//        }
//    }

//    public File[] getFileList(){
//        return fileList;
//    }

}
class Index implements Runnable
{
    //int id,File file, String[] stopWords,ArrayList<stopWordIndex> stopWordIndx
    int id1;
    int id2;
    File[] flist;
    TreeMap<String,Integer> stopWords;

    TreeMap<String,ArrayList > frequencyData ;
    ArrayList<termfreq> doclist;

    public Index(int id1,int id2,File[] flist, TreeMap<String,Integer> stopWords,TreeMap<String,ArrayList > frequencyData,ArrayList<termfreq> doclist ){
        this.id1=id1;//start of the file
        this.id2=id2;//end of the file
        this.flist=flist;//array of file name
        this.stopWords=stopWords;

        this.frequencyData=frequencyData;
        this.doclist=doclist;
    }

    public TreeMap getTree(){
        return frequencyData;
    }

    //thread will start from here
    public void run()
    {
        for(int id=id1;id<=id2;id++){
            String word;
            File file=flist[id];// read file by file
            String textFilename=file.getName();
            try
            {
                Scanner textFile = new Scanner(new FileReader(file));

                textFile.useDelimiter(Pattern.compile("<title>(.+?)</title>"));
                textFile.useDelimiter(Pattern.compile("[ \n\r\t,.;:?!'\"]+"));
                  while (textFile.hasNext())
                {
                    //read word from the file
                    word = textFile.next();
                    word = word.toLowerCase();
                    if(stopWords.containsKey(word)){
                    }
                    else{
                        //Check if the word already is in Dictionary
                        if (frequencyData.containsKey(word))
                        {  // The word has occurred before, so get its count from the map
                            @SuppressWarnings("unchecked")
                            ArrayList<termfreq> obj1=frequencyData.get(word);
                            int flag=0;
                            if(obj1!=null)
                                for(int x=0;x<obj1.size();x++){

                                    if(obj1.get(x)!=null)
                                        if(obj1.get(x).doc_id==id){
                                            obj1.get(x).term_freq++;
                                            flag=1;
                                            break;
                                        }
                                }
                            if(flag==0){
                                termfreq obj=new termfreq(id);
                                frequencyData.get(word).add(obj);
                            }
                        }else{
                            doclist=new ArrayList<termfreq>();
                            termfreq obj=new termfreq(id);
                            doclist.add(obj);
                            if(doclist!=null)
                                frequencyData.put(word,doclist);
                        }
                    }
                }
                textFile.close();
            }
            catch (FileNotFoundException e)
            {
                System.err.println(e.getMessage());
                System.exit(-1);
            }
        }
    }

    public static Map<String, String> getEntityTags(String text) throws IOException, ClassNotFoundException {
        String model = "english.muc.7class.distsim.crf.ser.gz";
        AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier.getClassifier(model);
        List<List<CoreLabel>> out = classifier.classify(text);
        Map<String, String> entityTagMap = new HashMap<>();
        for (List<CoreLabel> sentence : out) {
            for (CoreLabel word : sentence) {
                entityTagMap.put(word.word(),word.get(CoreAnnotations.AnswerAnnotation.class));
       System.out.println(word.word() + " - " + word.get(CoreAnnotations.AnswerAnnotation.class) );
            }
            System.out.println();
        }
        return entityTagMap;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        getEntityTags("shalinee is good girl. lucknow doing 8th january 2017 intel ");
    }

}
