package com.internstep.user.Models;

public class Jobs {
    public String company_logo;
    public String job_name;
    public boolean applied;
    public String job_description;
    public String responsibility;
    public String experience;
    public String job_edu;
    public String job_loc;
    public String lang;


    public String job_status;
    public String resume;
    public String software;
    public String ques1;
    public String ques2;


    public Jobs(String job_name,String responsibility,String experience,String job_edu,String job_loc,String lang,boolean applied, String job_description, String job_status, String resume, String software, String ques1, String ques2, String company_logo) {
        this.job_name = job_name;
        this.applied = applied;
        this.job_description = job_description;

        this.job_status = job_status;
        this.resume = resume;
        this.software = software;
        this.ques1 = ques1;
        this.ques2 = ques2;
        this.company_logo = company_logo;
        this.responsibility = responsibility;
        this.experience = experience;
        this.job_edu= job_edu;
        this.job_loc = job_loc;
        this.lang = lang;

    }

    public Jobs() {

    }

    public void setCompany_logo(String company_logo) {
        this.company_logo = company_logo;
    }


    public void setJob_name(String job_name) {
        this.job_name = job_name;
    }

    public void setApplied(boolean applied) {
        this.applied = applied;
    }

    public void setJob_description(String job_description) {
        this.job_description = job_description;
    }


    public void setJob_status(String job_status) {
        this.job_status = job_status;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public void setQues2(String ques2) {
        this.ques2 = ques2;
    }

    public void setSoftware(String software) {
        this.software = software;
    }

    public void setQues1(String ques1) {
        this.ques1 = ques1;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public void setJob_edu(String job_edu) {
        this.job_edu = job_edu;
    }

    public void setJob_loc(String job_loc) {
        this.job_loc = job_loc;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public void setResponsibility(String responsibility) {
        this.responsibility = responsibility;
    }

    public String getJob_description() {
        return job_description;
    }


    public String getJob_status() {
        return job_status;
    }

    public String getResume() {
        return resume;
    }

    public String getQues1() {
        return ques1;
    }

    public String getQues2() {
        return ques2;
    }

    public String getSoftware() {
        return software;
    }

    public boolean getApplied() {
        return applied;
    }

    public String getJob_name() {
        return job_name;
    }


    public String getCompany_logo() {
        return company_logo;
    }

    public String getResponsibility(){return responsibility;}

    public String getExperience(){return experience;}
    public String getJob_edu(){return  job_edu;}
    public String getJob_loc(){return job_loc;}
    public String getLang(){return lang;}
}
