package com.example.messmanagement;

public class Student {
    public String sname,sroll,semail,shostel;

    public Student(){

    }

    public String getShostel() {
        return shostel;
    }

    public void setShostel(String shostel) {
        this.shostel = shostel;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getSroll() {
        return sroll;
    }

    public void setSroll(String sroll) {
        this.sroll = sroll;
    }

    public String getSemail() {
        return semail;
    }

    public void setSemail(String semail) {
        this.semail = semail;
    }

    public Student(String sname, String sroll, String semail) {
        this.sname = sname;
        this.semail = semail;
        this.sroll = sroll;
    }
}
