package com.example.demo;

/**
 * Created by shalineesingh on 21/12/17.
 */
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;
import java.util.regex.Pattern;
import java.io.*;

public class DRelevance extends IndexerParaDocs {


//    public static void main(String[] args) throws IOException, ClassNotFoundException {
//        // TODO Auto-generated method stub
//        ArrayList<RelevantDocs> relvDocs= new ArrayList<RelevantDocs>();
//        TreeMap<String,ArrayList> Qrelevant = new TreeMap<String,ArrayList>();
//        findIndexer();
//
//        Scanner keyboard = new Scanner(System.in);
//    }



//    public static void NDCG(int[] rank){
//        //calculating DCG@20
//        double dcg=0.1;
//        dcg=dcg+rank[0]+1;
//        double idcg=0.1;
//
//        for(int i=1;i<20;i++){
//            //dcg@20
//            if((rank[i]+1)!=1){
//                dcg=dcg+ (double) (rank[i]+1)/(Math.log(rank[i]+1)/Math.log(2.0));
//            }
//        }
//        System.out.println("DCG:"+dcg);
//        //calculating idcg
//        int[] temp=rank;
//        Arrays.sort(temp);
//        idcg=temp[19]+1;
//        for(int i=18;i>=0;i--){
//            if((temp[i]+1)!=1){
//                idcg=idcg+ (temp[i]+1)/(Math.log(temp[i]+1)/Math.log(2.0));
//            }
//        }
//        double ndcg=(double) dcg/idcg;
//    }

//    public static void MeanRR(int[]  rank){
//        double tot=0;
//        for(int i=0;i<rank.length;i++){
//            if(rank[i]!=-1){
//                tot=tot+ (double)1/rank[i];
//            }
//        }
//    }
//
//    public static int compareRanks(TreeMap<String,ArrayList> Qrelevant,String[] token,int num)throws IOException,ClassNotFoundException{
//        //changed the rank function. It will now return String array of 20 documents name
//        String[] rankedDocs=rank(frequencyData,doclist,token,fileList);
//
//        int[] judgement=new int[20];
//        int numOfRelevantDocs=0;
//        int temp=num;
//        int rankPosOfFirstPos=-1;
//        temp=26-num;
//        String temp1;
//        if(temp>=10){
//            temp1=Integer.toString(temp);}else{
//            temp1="0"+Integer.toString(temp);
//        }
//        //iterate through all rank of this query
//        for(int i=0;i<rankedDocs.length;i++){
//            //iterate through all the judgement for this query.
//            int flag=0;
//            int fflag=0;
//            //iterate through all judgement for particular query.
//            for(int x=0;x<Qrelevant.get(temp1).size();x++){
//                RelevantDocs obj1= (RelevantDocs) Qrelevant.get(temp1).get(x);
//                //if the name of the file in my rank equals name in judgement
//                if(obj1.fileName.equals(rankedDocs[i])){
//                    judgement[i]=(int) obj1.relevantJudgement;
//                    if(obj1.relevantJudgement>=1){
//                        numOfRelevantDocs++;
//                    }
//                    flag=1;
//                    fflag=1;
//                    break;
//                }
//            }
//            if(numOfRelevantDocs==1){
//                rankPosOfFirstPos=i;
//                fflag=0;
//            }
//            if(flag==0){
//                judgement[i]=-1;
//            }
//
//        }
//        computePrecision(judgement,temp1);
//        computeRecall(judgement,temp1,numOfRelevantDocs);
//        NDCG(judgement);
//        System.out.println("----------------------------------------------------------------------------------");
//        System.out.println();
//        return rankPosOfFirstPos;
//    }
//
//    public static void computePrecision(int[] rank,String Querynum){
//        int[] rank1=rank;
//        int count=0;
//        double averageP=0.0;
//        double count2=0.1;
//        System.out.print("Precision for query num:===>"+Querynum+"|      ");
//        for(int i=0;i<rank1.length;i++){
//            int temp=i+1;
//            if(rank1[i]>=1){
//                count++;
//                count2++;
//
//                System.out.print((double)count/temp + "|     ");
//                averageP=averageP + (double) count/temp;
//            }
//        }
//        double temp=(double) averageP/count2;
//        System.out.println(" Average Precision is :"+ temp);
//
//    }
//
//    public static void computeRecall(int[] rank,String Querynum,int numOfRelevantDocs){
//        int[] rank1=rank;
//        int count=0;
//        double averageP=0.0;
//        double count2=0.0;
//        System.out.print("Recall for query num:===>"+Querynum+"|      ");
//        for(int i=0;i<rank1.length;i++){
//            int temp=i+1;
//            if(rank1[i]>=1){
//                count++;
//
//                System.out.print((double) count/numOfRelevantDocs + "|     ");
//
//            }
//        }
//    }
//    private ArrayList<RelevantDocs> relvDocs= new ArrayList<RelevantDocs>();
//    private TreeMap<String,ArrayList> Qrelevant = new TreeMap<String,ArrayList>();
}

