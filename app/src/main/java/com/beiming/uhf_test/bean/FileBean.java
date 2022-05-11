package com.beiming.uhf_test.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by htj on 2019/8/7.
 */
@Entity
public class FileBean implements Serializable {
    private static final long serialVersionUID = 0x111111113L;
    @Id(autoincrement = true)
    private Long fileId;//序号

    private String fileType;//文件类型 1:pdf,2:视频,3:excel
    private String uploadFileType;//文件类型 1:说明书pdf,2:安装指导视频,3:安装指导pdf,4:操作维护视频,5:操作维护pdf
    private String fileName;//文件名
    private String res;  //文件路径
    private String createTime;  //图片创建时间
    private String device_id;  //所对应的设备唯一识别码(子设备DevicesBean中的devNameCN)
    @Generated(hash = 312915004)
    public FileBean(Long fileId, String fileType, String uploadFileType, String fileName,
            String res, String createTime, String device_id) {
        this.fileId = fileId;
        this.fileType = fileType;
        this.uploadFileType = uploadFileType;
        this.fileName = fileName;
        this.res = res;
        this.createTime = createTime;
        this.device_id = device_id;
    }
    @Generated(hash = 1910776192)
    public FileBean() {
    }
    public Long getFileId() {
        return this.fileId;
    }
    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }
    public String getFileType() {
        return this.fileType;
    }
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
    public String getUploadFileType() {
        return this.uploadFileType;
    }
    public void setUploadFileType(String uploadFileType) {
        this.uploadFileType = uploadFileType;
    }
    public String getFileName() {
        return this.fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getRes() {
        return this.res;
    }
    public void setRes(String res) {
        this.res = res;
    }
    public String getCreateTime() {
        return this.createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public String getDevice_id() {
        return this.device_id;
    }
    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

}
