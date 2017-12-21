package com.example.demo;

import org.springframework.stereotype.Service;

/**
 * Created by shalineesingh on 21/12/17.
 */

public class RelevantDocs{
    public String fileName;
    public int relevantJudgement;

    public RelevantDocs(String file,int judge){
        this.fileName=file;
        this.relevantJudgement=judge;
    }

}