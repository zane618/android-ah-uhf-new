package com.beiming.uhf_test.bean;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

/**
 * 库房资产
 */
@Entity
public class LibAssetBean implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id(autoincrement = true)
    private Long assetId;//序号

    private String danwei;
    private String danweiCode;
    private String kufang;
    private String kufangCode;
    private String kuqu;
    private String kuquCode;
    private String barCode;//条形码编号，资产库没有这个
    private String assetNo;//资产编号
    private String state;
    private String stateCode;
    private boolean exsit;//本地是否存在


    public Long getAssetId() {
        return assetId;
    }

    public void setAssetId(Long assetId) {
        this.assetId = assetId;
    }

    public String getDanwei() {
        return danwei;
    }

    public void setDanwei(String danwei) {
        this.danwei = danwei;
    }

    public String getDanweiCode() {
        return danweiCode;
    }

    public void setDanweiCode(String danweiCode) {
        this.danweiCode = danweiCode;
    }

    public String getKufang() {
        return kufang;
    }

    public void setKufang(String kufang) {
        this.kufang = kufang;
    }

    public String getKufangCode() {
        return kufangCode;
    }

    public void setKufangCode(String kufangCode) {
        this.kufangCode = kufangCode;
    }

    public String getKuqu() {
        return kuqu;
    }

    public void setKuqu(String kuqu) {
        this.kuqu = kuqu;
    }

    public String getKuquCode() {
        return kuquCode;
    }

    public void setKuquCode(String kuquCode) {
        this.kuquCode = kuquCode;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getAssetNo() {
        return assetNo;
    }

    public void setAssetNo(String assetNo) {
        this.assetNo = assetNo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public boolean isExsit() {
        return exsit;
    }

    public void setExsit(boolean exsit) {
        this.exsit = exsit;
    }
}
