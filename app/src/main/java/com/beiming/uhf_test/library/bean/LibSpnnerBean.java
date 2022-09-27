package com.beiming.uhf_test.library.bean;

import java.io.Serializable;

public class LibSpnnerBean implements Serializable {

    private String assectType = "";
    private String assectName = "";

    public LibSpnnerBean(String assectName, String assectType) {
        this.assectType = assectType;
        this.assectName = assectName;
    }

    public String getAssectType() {
        return assectType;
    }

    public void setAssectType(String assectType) {
        this.assectType = assectType;
    }

    public String getAssectName() {
        return assectName;
    }

    public void setAssectName(String assectName) {
        this.assectName = assectName;
    }
}
