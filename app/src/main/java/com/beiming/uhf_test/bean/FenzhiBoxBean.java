package com.beiming.uhf_test.bean;


import com.beiming.uhf_test.bean.pic.PhotoBean;
import com.beiming.uhf_test.utils.GreenDaoUtil.MeterBeanConverter;
import com.beiming.uhf_test.utils.GreenDaoUtil.PhotoBeanConverter;
import com.beiming.uhf_test.utils.GreenDaoUtil.StringConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import java.util.List;

/**
 * 箱
 * 条形码bean
 * Created by htj on 2021/5/20.
 */
@Entity
public class FenzhiBoxBean implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id(autoincrement = true)
    private Long FenzhiId;//序号

    private String barCode;//电表箱条形码编号
    private String assetNo;//电表箱资产编号
    private String scanTime;//扫描时间
    private long ts; //同上，时间戳
    private String gps_X;//GPS经度,Longitude
    private String gps_Y;//GPS纬度,Latitude
    private String gps_Z;//GPS海拔
    private String instAddr = "暂无位置";//安装地址,目前的定位
    private String note = "未备注";//备注
    @Convert(columnType = String.class, converter = StringConverter.class)
    private List<String> measboxBars; //箱子标号集合
    private boolean exsit;//本地是否存在
    private boolean checked; //tmp 本地使用 是否选中

    @Generated(hash = 345641021)
    public FenzhiBoxBean(Long FenzhiId, String barCode, String assetNo,
            String scanTime, long ts, String gps_X, String gps_Y, String gps_Z,
            String instAddr, String note, List<String> measboxBars, boolean exsit,
            boolean checked) {
        this.FenzhiId = FenzhiId;
        this.barCode = barCode;
        this.assetNo = assetNo;
        this.scanTime = scanTime;
        this.ts = ts;
        this.gps_X = gps_X;
        this.gps_Y = gps_Y;
        this.gps_Z = gps_Z;
        this.instAddr = instAddr;
        this.note = note;
        this.measboxBars = measboxBars;
        this.exsit = exsit;
        this.checked = checked;
    }

    @Generated(hash = 1699667973)
    public FenzhiBoxBean() {
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public Long getFenzhiId() {
        return FenzhiId;
    }

    public void setFenzhiId(Long fenzhiId) {
        FenzhiId = fenzhiId;
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

    public String getScanTime() {
        return scanTime;
    }

    public void setScanTime(String scanTime) {
        this.scanTime = scanTime;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public String getGps_X() {
        return gps_X;
    }

    public void setGps_X(String gps_X) {
        this.gps_X = gps_X;
    }

    public String getGps_Y() {
        return gps_Y;
    }

    public void setGps_Y(String gps_Y) {
        this.gps_Y = gps_Y;
    }

    public String getGps_Z() {
        return gps_Z;
    }

    public void setGps_Z(String gps_Z) {
        this.gps_Z = gps_Z;
    }

    public String getInstAddr() {
        return instAddr;
    }

    public void setInstAddr(String instAddr) {
        this.instAddr = instAddr;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<String> getMeasboxBars() {
        return measboxBars;
    }

    public void setMeasboxBars(List<String> measboxBars) {
        this.measboxBars = measboxBars;
    }

    public boolean isExsit() {
        return exsit;
    }

    public void setExsit(boolean exsit) {
        this.exsit = exsit;
    }

    public boolean getExsit() {
        return this.exsit;
    }

    public boolean getChecked() {
        return this.checked;
    }
}
