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
    private long ts; //同上，时间戳
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
    private String note = "未备注";//备注
    private boolean isExsit;//本地是否存在
    private String hasQx; //是否缺陷，无，一般、严重、危急
    private String qxDetail; //缺陷详情, 逗号分割
    private String fenzhixCode; //分支箱编码
    private String fenzhixAssetNo; //分支箱资产编码
    private String caizhi = "金属"; //金属，非金属
    private String gao = ""; //长
    private String kuan = ""; //宽
    private String zsGao = "";
    private String zsKuan = "";
    private String zxGao = "";
    private String zxKuan = "";
    private String ysGao = "";
    private String ysKuan = "";
    private String yxGao = "";
    private String yxKuan = "";
    private boolean checked; //tmp 本地使用 是否选中


    @Convert(columnType = String.class, converter = MeterBeanConverter.class)
    private List<MeterBean> meters;//计量箱下电能表的集合

    @Convert(columnType = String.class, converter = PhotoBeanConverter.class)
    private List<PhotoBean> boxImages;//计量箱下图片的集合

    @Generated(hash = 2071421462)
    public MeasBoxBean(Long MeasBoxId, String barCode, String measAssetNo, String scanTime,
            long ts, String gps_X, String gps_Y, String gps_Z, String instAddr,
            String instLoc, String describe, String tmnlAddr, String tgName, String boxRows,
            String boxCols, String note, boolean isExsit, String hasQx, String qxDetail,
            String fenzhixCode, String fenzhixAssetNo, String caizhi, String gao, String kuan,
            String zsGao, String zsKuan, String zxGao, String zxKuan, String ysGao,
            String ysKuan, String yxGao, String yxKuan, boolean checked,
            List<MeterBean> meters, List<PhotoBean> boxImages) {
        this.MeasBoxId = MeasBoxId;
        this.barCode = barCode;
        this.measAssetNo = measAssetNo;
        this.scanTime = scanTime;
        this.ts = ts;
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
        this.qxDetail = qxDetail;
        this.fenzhixCode = fenzhixCode;
        this.fenzhixAssetNo = fenzhixAssetNo;
        this.caizhi = caizhi;
        this.gao = gao;
        this.kuan = kuan;
        this.zsGao = zsGao;
        this.zsKuan = zsKuan;
        this.zxGao = zxGao;
        this.zxKuan = zxKuan;
        this.ysGao = ysGao;
        this.ysKuan = ysKuan;
        this.yxGao = yxGao;
        this.yxKuan = yxKuan;
        this.checked = checked;
        this.meters = meters;
        this.boxImages = boxImages;
    }

    @Generated(hash = 1045529700)
    public MeasBoxBean() {
    }

    public String getFenzhixAssetNo() {
        return fenzhixAssetNo;
    }

    public void setFenzhixAssetNo(String fenzhixAssetNo) {
        this.fenzhixAssetNo = fenzhixAssetNo;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public String getFenzhixCode() {
        return fenzhixCode;
    }

    public void setFenzhixCode(String fenzhixCode) {
        this.fenzhixCode = fenzhixCode;
    }

    public String getQxDetail() {
        return qxDetail;
    }

    public void setQxDetail(String qxDetail) {
        this.qxDetail = qxDetail;
    }

    public String getGao() {
        return gao;
    }

    public void setGao(String gao) {
        this.gao = gao;
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

    public boolean getChecked() {
        return this.checked;
    }
}
