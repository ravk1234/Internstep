package com.internstep.user.Models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Companies implements Serializable {
    private String company_name;
    private String company_logo;
    private int open_positions;

    private Map<String,Jobs> job_roles = new HashMap<>();

    private String location;




    public Companies(){

    }

    public Companies(String company_name,String company_logo,int open_positions,Map<String,Jobs> job_roles,
                     String location){
        this.company_name = company_name;
        this.company_logo = company_logo;
        this.open_positions = open_positions;
        this.job_roles = job_roles;
        this.location = location;


    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public void setCompany_logo(String company_logo) {
        this.company_logo = company_logo;
    }

    public void setOpen_positions(int open_positions) {
        this.open_positions = open_positions;
    }




    public void setLocation(String location) {
        this.location = location;
    }




    public String getCompany_name() {
        return company_name;
    }

    public String getCompany_logo() {
        return company_logo;
    }


    public int getOpen_positions() {
        return open_positions;
    }



    public String getLocation() {
        return location;
    }



    public void setJob_roles(Map<String,Jobs> job_roles) {
        this.job_roles = job_roles;
    }

    public Map<String,Jobs> getJob_roles() {
        return job_roles;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }


}
