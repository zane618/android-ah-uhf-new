package com.beiming.uhf_test.library.adapter;

public class SpinnerData {
    private int logoId;
    private String name;
    private String comment;


    public SpinnerData(){}

    public SpinnerData(int logoId, String name, String comment) {
        this.logoId = logoId;
        this.name = name;
        this.comment = comment;
    }

    public int getLogoId() {
        return logoId;
    }

    public void setLogoId(int logoId) {
        this.logoId = logoId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
