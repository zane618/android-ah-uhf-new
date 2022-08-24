package com.beiming.uhf_test.bean.pic;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

/**
 * Created by htj on 2018/11/8.
 */
@Entity
public class PhotoBean implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id(autoincrement = true)
    private Long photoBeanId;//序号

    private String measAssetBarCode;//所属电表箱条形码

    private String createTime;//创建时间

    private String describe;//描述

    private String imageSrc;//图片地址

    private String picName;//图片名称

    private String add; //1 == 1时是添加按钮

    private int type; //图片类型

    @Generated(hash = 788672481)
    public PhotoBean(Long photoBeanId, String measAssetBarCode, String createTime,
            String describe, String imageSrc, String picName, String add,
            int type) {
        this.photoBeanId = photoBeanId;
        this.measAssetBarCode = measAssetBarCode;
        this.createTime = createTime;
        this.describe = describe;
        this.imageSrc = imageSrc;
        this.picName = picName;
        this.add = add;
        this.type = type;
    }

    @Override
    public String toString() {
        return "PhotoBean{" +
                "photoBeanId=" + photoBeanId +
                ", measAssetBarCode='" + measAssetBarCode + '\'' +
                ", createTime='" + createTime + '\'' +
                ", describe='" + describe + '\'' +
                ", imageSrc='" + imageSrc + '\'' +
                ", picName='" + picName + '\'' +
                '}';
    }

    public PhotoBean(int type) {
        this.type = type;
    }

    public PhotoBean() {
    }


    public void setAdd(String add) {
        this.add = add;
    }

    public String getAdd() {
        return add;
    }

    public boolean isAdd() {
        return "1".equals(add);
    }


    public Long getPhotoBeanId() {
        return photoBeanId;
    }

    public void setPhotoBeanId(Long photoBeanId) {
        this.photoBeanId = photoBeanId;
    }

    public String getMeasAssetBarCode() {
        return measAssetBarCode;
    }

    public void setMeasAssetBarCode(String measAssetBarCode) {
        this.measAssetBarCode = measAssetBarCode;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
