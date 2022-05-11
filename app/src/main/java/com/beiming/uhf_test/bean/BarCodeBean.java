package com.beiming.uhf_test.bean;

import java.io.Serializable;

/**
 * 条形码bean
 * Created by htj on 2021/5/20.
 */
public class BarCodeBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private String tagUii;//扫描时接收到的标签值
    private String barCode;//截取后的条形码编号
    private String barCodeType;//条形码类型 0:计量箱 ，1：电能表 -1:不能识别的条形码
    private int tagCount;//标签扫描到的次数
    private String tagRssi;//标签扫描所用的时间
    private String scanTime;//扫描时间
    private boolean isExsit;//本地是否存在

    public BarCodeBean() {
    }

    public BarCodeBean(String tagUii, String barCode, String barCodeType, int tagCount, String tagRssi, String scanTime) {
        this.tagUii = tagUii;
        this.barCode = barCode;
        this.barCodeType = barCodeType;
        this.tagCount = tagCount;
        this.tagRssi = tagRssi;
        this.scanTime = scanTime;
    }

    public String getTagUii() {
        return tagUii;
    }

    public void setTagUii(String tagUii) {
        this.tagUii = tagUii;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getBarCodeType() {
        return barCodeType;
    }

    public void setBarCodeType(String barCodeType) {
        this.barCodeType = barCodeType;
    }


    public int getTagCount() {
        return tagCount;
    }

    public void setTagCount(int tagCount) {
        this.tagCount = tagCount;
    }

    public String getTagRssi() {
        return tagRssi;
    }

    public void setTagRssi(String tagRssi) {
        this.tagRssi = tagRssi;
    }

    public String getScanTime() {
        return scanTime;
    }

    public void setScanTime(String scanTime) {
        this.scanTime = scanTime;
    }

    public boolean isExsit() {
        return isExsit;
    }

    public void setExsit(boolean exsit) {
        isExsit = exsit;
    }
}
