package com.beiming.uhf_test.bean;


import com.beiming.uhf_test.bean.pic.PhotoBean;
import com.beiming.uhf_test.utils.GreenDaoUtil.MeterBeanConverter;
import com.beiming.uhf_test.utils.GreenDaoUtil.PhotoBeanConverter;

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
public class MeasBoxBean implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id(autoincrement = true)
    private Long MeasBoxId;//序号

    private String barCode;//电表箱条形码编号
    private String measAssetNo;//电表箱资产编号
    private String scanTime;//扫描时间
    private String gps_X;//GPS经度,Longitude
    private String gps_Y;//GPS纬度,Latitude
    private String gps_Z;//GPS海拔
    private String instAddr;//安装地址,目前的定位
    private String instLoc;//安装位置
    private String describe;//描述
    private String tmnlAddr;//终端地址
    private String tgName;  //台区名称
    private String boxRows;//行数
    private String boxCols;//列数
    private String note = "无备注";//备注
    private boolean isExsit;//本地是否存在
    private String hasQx; //是否缺陷，0是，1否
    private String qxJiaolian; //铰链缺陷，0是，1否
    private String qxLaogu; //牢固缺陷，0是，1否
    private String qxSuo; //锁缺陷，0是，1否
    private String qxZawu; //杂物缺陷，0是，1否
    private String caizhi = "金属"; //金属，非金属
    private String chang = ""; //长
    private String kuan = ""; //宽
    private String zsGao = "";
    private String zsKuan = "";
    private String zxGao = "";
    private String zxKuan = "";
    private String ysGao = "";
    private String ysKuan = "";
    private String yxGao = "";
    private String yxKuan = "";


    @Convert(columnType = String.class, converter = MeterBeanConverter.class)
    private List<MeterBean> meters;//计量箱下电能表的集合

    @Convert(columnType = String.class, converter = PhotoBeanConverter.class)
    private List<PhotoBean> boxImages;//计量箱下图片的集合

    @Generated(hash = 556139876)
    public MeasBoxBean(Long MeasBoxId, String barCode, String measAssetNo, String scanTime,
            String gps_X, String gps_Y, String gps_Z, String instAddr, String instLoc,
            String describe, String tmnlAddr, String tgName, String boxRows, String boxCols,
            String note, boolean isExsit, String hasQx, String qxJiaolian, String qxLaogu,
            String qxSuo, String qxZawu, String caizhi, String chang, String kuan,
            String zsGao, String zsKuan, String zxGao, String zxKuan, String ysGao,
            String ysKuan, String yxGao, String yxKuan, List<MeterBean> meters,
            List<PhotoBean> boxImages) {
        this.MeasBoxId = MeasBoxId;
        this.barCode = barCode;
        this.measAssetNo = measAssetNo;
        this.scanTime = scanTime;
        this.gps_X = gps_X;
        this.gps_Y = gps_Y;
        this.gps_Z = gps_Z;
        this.instAddr = instAddr;
        this.instLoc = instLoc;
        this.describe = describe;
        this.tmnlAddr = tmnlAddr;
        this.tgName = tgName;
        this.boxRows = boxRows;
        this.boxCols = boxCols;
        this.note = note;
        this.isExsit = isExsit;
        this.hasQx = hasQx;
        this.qxJiaolian = qxJiaolian;
        this.qxLaogu = qxLaogu;
        this.qxSuo = qxSuo;
        this.qxZawu = qxZawu;
        this.caizhi = caizhi;
        this.chang = chang;
        this.kuan = kuan;
        this.zsGao = zsGao;
        this.zsKuan = zsKuan;
        this.zxGao = zxGao;
        this.zxKuan = zxKuan;
        this.ysGao = ysGao;
        this.ysKuan = ysKuan;
        this.yxGao = yxGao;
        this.yxKuan = yxKuan;
        this.meters = meters;
        this.boxImages = boxImages;
    }

    @Generated(hash = 1045529700)
    public MeasBoxBean() {
    }

    public String getChang() {
        return chang;
    }

    public void setChang(String chang) {
        this.chang = chang;
    }

    public String getKuan() {
        return kuan;
    }

    public void setKuan(String kuan) {
        this.kuan = kuan;
    }

    public String getCaizhi() {
        return caizhi;
    }

    public void setCaizhi(String caizhi) {
        this.caizhi = caizhi;
    }

    public String getHasQx() {
        return hasQx;
    }

    public void setHasQx(String hasQx) {
        this.hasQx = hasQx;
    }

    public String getQxJiaolian() {
        return qxJiaolian;
    }

    public void setQxJiaolian(String qxJiaolian) {
        this.qxJiaolian = qxJiaolian;
    }

    public String getQxLaogu() {
        return qxLaogu;
    }

    public void setQxLaogu(String qxLaogu) {
        this.qxLaogu = qxLaogu;
    }

    public String getQxSuo() {
        return qxSuo;
    }

    public void setQxSuo(String qxSuo) {
        this.qxSuo = qxSuo;
    }

    public String getQxZawu() {
        return qxZawu;
    }

    public void setQxZawu(String qxZawu) {
        this.qxZawu = qxZawu;
    }

    public Long getMeasBoxId() {
        return this.MeasBoxId;
    }

    public void setMeasBoxId(Long MeasBoxId) {
        this.MeasBoxId = MeasBoxId;
    }

    public String getBarCode() {
        return this.barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getMeasAssetNo() {
        return this.measAssetNo;
    }

    public void setMeasAssetNo(String measAssetNo) {
        this.measAssetNo = measAssetNo;
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

    public String getInstAddr() {
        return this.instAddr;
    }

    public void setInstAddr(String instAddr) {
        this.instAddr = instAddr;
    }

    public String getInstLoc() {
        return this.instLoc;
    }

    public void setInstLoc(String instLoc) {
        this.instLoc = instLoc;
    }

    public String getDescribe() {
        return this.describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getTmnlAddr() {
        return this.tmnlAddr;
    }

    public void setTmnlAddr(String tmnlAddr) {
        this.tmnlAddr = tmnlAddr;
    }

    public String getTgName() {
        return this.tgName;
    }

    public void setTgName(String tgName) {
        this.tgName = tgName;
    }

    public String getBoxRows() {
        return this.boxRows;
    }

    public void setBoxRows(String boxRows) {
        this.boxRows = boxRows;
    }

    public String getBoxCols() {
        return this.boxCols;
    }

    public void setBoxCols(String boxCols) {
        this.boxCols = boxCols;
    }

    public List<MeterBean> getMeters() {
        return this.meters;
    }

    public void setMeters(List<MeterBean> meters) {
        this.meters = meters;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<PhotoBean> getBoxImages() {
        return this.boxImages;
    }

    public void setBoxImages(List<PhotoBean> boxImages) {
        this.boxImages = boxImages;
    }

    @Override
    public String toString() {
        return "MeasBoxBean{" +
                "MeasBoxId=" + MeasBoxId +
                ", barCode='" + barCode + '\'' +
                ", measAssetNo='" + measAssetNo + '\'' +
                ", scanTime='" + scanTime + '\'' +
                ", gps_X='" + gps_X + '\'' +
                ", gps_Y='" + gps_Y + '\'' +
                ", gps_Z='" + gps_Z + '\'' +
                ", instAddr='" + instAddr + '\'' +
                ", instLoc='" + instLoc + '\'' +
                ", describe='" + describe + '\'' +
                ", tmnlAddr='" + tmnlAddr + '\'' +
                ", tgName='" + tgName + '\'' +
                ", boxRows='" + boxRows + '\'' +
                ", boxCols='" + boxCols + '\'' +
                ", note='" + note + '\'' +
                ", isExsit=" + isExsit +
                ", meters=" + meters +
                ", boxImages=" + boxImages +
                '}';
    }

    public boolean getIsExsit() {
        return this.isExsit;
    }

    public void setIsExsit(boolean isExsit) {
        this.isExsit = isExsit;
    }

    public boolean equals(Object obj) {
        MeasBoxBean u = (MeasBoxBean) obj;
        return barCode.equals(u.barCode);
    }

    public int hashCode() {
        String in = barCode;
        return in.hashCode();
    }

    public String getZsGao() {
        return this.zsGao;
    }

    public void setZsGao(String zsGao) {
        this.zsGao = zsGao;
    }

    public String getZsKuan() {
        return this.zsKuan;
    }

    public void setZsKuan(String zsKuan) {
        this.zsKuan = zsKuan;
    }

    public String getZxGao() {
        return this.zxGao;
    }

    public void setZxGao(String zxGao) {
        this.zxGao = zxGao;
    }

    public String getZxKuan() {
        return this.zxKuan;
    }

    public void setZxKuan(String zxKuan) {
        this.zxKuan = zxKuan;
    }

    public String getYsGao() {
        return this.ysGao;
    }

    public void setYsGao(String ysGao) {
        this.ysGao = ysGao;
    }

    public String getYsKuan() {
        return this.ysKuan;
    }

    public void setYsKuan(String ysKuan) {
        this.ysKuan = ysKuan;
    }

    public String getYxGao() {
        return this.yxGao;
    }

    public void setYxGao(String yxGao) {
        this.yxGao = yxGao;
    }

    public String getYxKuan() {
        return this.yxKuan;
    }

    public void setYxKuan(String yxKuan) {
        this.yxKuan = yxKuan;
    }
}
