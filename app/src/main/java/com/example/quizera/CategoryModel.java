package com.example.quizera;

public class CategoryModel {
    private String docID;
    private String name;
    private long time;


    public CategoryModel(String docID, String name,long time) {
        this.docID = docID;
        this.name = name;
        this.time = time;

    }


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



}



