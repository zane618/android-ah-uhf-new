package com.beiming.uhf_test.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

/**
 * 条形码bean
 * Created by htj on 2021/5/20.
 */
@Entity
public class MeterBean implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id(autoincrement = true)
    private Long meterId;//序号

    private String barCode;//扫描到的条形码编号
    private String meterAssetNo;//电表资产编号
    private String measAssetNo;//电表箱资产编号
    private String measBarCode;//电表箱条形码
    private String scanTime;//扫描时间
    private String gps_X;//GPS经度,Longitude
    private String gps_Y;//GPS纬度,Latitude
    private String gps_Z;//GPS海拔
    private String gpsLatitude;//GPS纬度
    private String gpsLongitude;//GPS经度
    private boolean isExsit;//本地是否存在
    private String phase;//电表接入的相位信息，有四种值：a，b，c，空
    @Generated(hash = 1903856073)
    public MeterBean(Long meterId, String barCode, String meterAssetNo,
            String measAssetNo, String measBarCode, String scanTime, String gps_X,
            String gps_Y, String gps_Z, String gpsLatitude, String gpsLongitude,
            boolean isExsit, String phase) {
        this.meterId = meterId;
        this.barCode = barCode;
        this.meterAssetNo = meterAssetNo;
        this.measAssetNo = measAssetNo;
        this.measBarCode = measBarCode;
        this.scanTime = scanTime;
        this.gps_X = gps_X;
        this.gps_Y = gps_Y;
        this.gps_Z = gps_Z;
        this.gpsLatitude = gpsLatitude;
        this.gpsLongitude = gpsLongitude;
        this.isExsit = isExsit;
        this.phase = phase;
    }
    @Generated(hash = 2099306990)
    public MeterBean() {
    }
    public Long getMeterId() {
        return this.meterId;
    }
    public void setMeterId(Long meterId) {
        this.meterId = meterId;
    }
    public String getBarCode() {
        return this.barCode;
    }
    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }
    public String getMeterAssetNo() {
        return this.meterAssetNo;
    }
    public void setMeterAssetNo(String meterAssetNo) {
        this.meterAssetNo = meterAssetNo;
    }
    public String getMeasAssetNo() {
        return this.measAssetNo;
    }
    public void setMeasAssetNo(String measAssetNo) {
        this.measAssetNo = measAssetNo;
    }
    public String getMeasBarCode() {
        return this.measBarCode;
    }
    public void setMeasBarCode(String measBarCode) {
        this.measBarCode = measBarCode;
    }
    public String getScanTime() {
        return this.scanTime;
    }
    public void setScanTime(String scanTime) {
        this.scanTime = scanTime;
    }
    public String getGps_X() {
        return this.gps_X;
    }
    public void setGps_X(String gps_X) {
        this.gps_X = gps_X;
    }
    public String getGps_Y() {
        return this.gps_Y;
    }
    public void setGps_Y(String gps_Y) {
        this.gps_Y = gps_Y;
    }
    public String getGps_Z() {
        return this.gps_Z;
    }
    public void setGps_Z(String gps_Z) {
        this.gps_Z = gps_Z;
    }
    public String getGpsLatitude() {
        return this.gpsLatitude;
    }
    public void setGpsLatitude(String gpsLatitude) {
        this.gpsLatitude = gpsLatitude;
    }
    public String getGpsLongitude() {
        return this.gpsLongitude;
    }
    public void setGpsLongitude(String gpsLongitude) {
        this.gpsLongitude = gpsLongitude;
    }
    public boolean getIsExsit() {
        return this.isExsit;
    }
    public void setIsExsit(boolean isExsit) {
        this.isExsit = isExsit;
    }
    public String getPhase() {
        return this.phase;
    }
    public void setPhase(String phase) {
        this.phase = phase;
    }

    @Override
    public String toString() {
        return "MeterBean{" +
                "meterId=" + meterId +
                ", barCode='" + barCode + '\'' +
                ", meterAssetNo='" + meterAssetNo + '\'' +
                ", measAssetNo='" + measAssetNo + '\'' +
                ", measBarCode='" + measBarCode + '\'' +
                ", scanTime='" + scanTime + '\'' +
                ", gps_X='" + gps_X + '\'' +
                ", gps_Y='" + gps_Y + '\'' +
                ", gps_Z='" + gps_Z + '\'' +
                ", gpsLatitude='" + gpsLatitude + '\'' +
                ", gpsLongitude='" + gpsLongitude + '\'' +
                ", isExsit=" + isExsit +
                ", phase='" + phase + '\'' +
                '}';
    }
}
