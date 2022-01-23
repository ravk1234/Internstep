package com.internstep.user.Models;

import java.io.Serializable;
import java.util.ArrayList;


public class User implements Serializable {
    private String uid;
    private String fullname;
    private String emailAddress;
    private String  phoneNumber;
    private String gender;
    private String dob;
    private String idcard;
    private String short_description;
    private String job_title;
    private String education;
    private String linkedin;
    private String github;
    private String dribble;
    private String imgUrl;
    private ArrayList<Companies> companies;




    public User(String uid,String firstname,String email, String phoneNumber, String gender,String dob,String
            short_description,String job_title, String education,String linkedin,String github,String dribble,String imgUrl
            ,ArrayList<Companies> company,String idcard) {
        this.uid = uid;
        this.fullname = firstname;
        this.emailAddress=email;
        this.phoneNumber = phoneNumber;
        this.gender=gender;
        this.dob = dob;
        this.short_description = short_description;
        this.job_title = job_title;
        this.education = education;
        this.linkedin = linkedin;
        this.github = github;
        this.dribble = dribble;
        this.imgUrl = imgUrl;
        this.companies = company;

        this.idcard = idcard;
    }

    public User(){
        // Default constructor required
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }


    public void setUid(String uid){this.uid = uid;}

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public void setGithub(String github) {
        this.github = github;
    }

    public void setDribble(String dribble) {
        this.dribble = dribble;
    }

    public void setName(String fullname){this.fullname = fullname;}
    public void setEmailAddress(String emailAddress){this.emailAddress = emailAddress;}
    public void setPhoneNumber(String phoneNumber){this.phoneNumber = phoneNumber;}
    public void setGender(String gender){this.gender = gender;}

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setCompanies(ArrayList<Companies> companies) {
        this.companies = companies;
    }

    public String getIdcard() {
        return idcard;
    }

    public String getDob() {
        return dob;
    }
    public String getUID(){return uid;}
    public String getName(){ return fullname; }
    public String getEmailAddress(){ return emailAddress; }
    public String getPhoneNumber(){ return phoneNumber; }
    public String getGender(){ return  gender; }


    public String getShort_description() {
        return short_description;
    }

    public String getJob_title() {
        return job_title;
    }

    public String getEducation() {
        return education;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public String getGithub() {
        return github;
    }

    public String getDribble() {
        return dribble;
    }


    public String getImgUrl() {
        return imgUrl;
    }



    public ArrayList<Companies> getCompanies() {
        return companies;
    }
}
