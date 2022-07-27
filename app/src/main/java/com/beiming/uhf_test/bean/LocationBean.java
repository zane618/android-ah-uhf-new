package com.beiming.uhf_test.bean;

import java.io.Serializable;

/**
 * 条形码bean
 * Created by htj on 2021/5/20.
 */
public class LocationBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private String latitude;//获取纬度
    private String longitude;//获取经度
    private String accuracy;//获取精度信息
    private String address;//地址
    private String country;//国家信息
    private String province;//省信息
    private String city;//城市信息
    private String district;//城区信息
    private String street;//街道信息
    private String streetNum;//街道门牌号信息
    private String aoiName;//获取当前定位点的AOI信息
    private String buildingId;//获取当前室内定位的建筑物Id
    private String floor;//获取当前室内定位的楼层
    private String gpsAccuracyStatus;//获取GPS的当前状态
    private String time;//获取定位的时间
    private String cityCode;//城市区号
    private String adCode;//行政区号

    private String errorInfo;                //定位失败信息

    public LocationBean() {
    }

    public LocationBean(String latitude, String longitude, String address) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    @Override
    public String toString() {
        return "LocationBean{" +
                "latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", accuracy='" + accuracy + '\'' +
                ", address='" + address + '\'' +
                ", country='" + country + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", street='" + street + '\'' +
                ", streetNum='" + streetNum + '\'' +
                ", aoiName='" + aoiName + '\'' +
                ", buildingId='" + buildingId + '\'' +
                ", floor='" + floor + '\'' +
                ", gpsAccuracyStatus='" + gpsAccuracyStatus + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getAdCode() {
        return adCode;
    }

    public void setAdCode(String adCode) {
        this.adCode = adCode;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNum() {
        return streetNum;
    }

    public void setStreetNum(String streetNum) {
        this.streetNum = streetNum;
    }

    public String getAoiName() {
        return aoiName;
    }

    public void setAoiName(String aoiName) {
        this.aoiName = aoiName;
    }

    public String getBuildingId() {
        return buildingId;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public void setBuildingId(String buildingId) {
        this.buildingId = buildingId;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getGpsAccuracyStatus() {
        return gpsAccuracyStatus;
    }

    public void setGpsAccuracyStatus(String gpsAccuracyStatus) {
        this.gpsAccuracyStatus = gpsAccuracyStatus;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
