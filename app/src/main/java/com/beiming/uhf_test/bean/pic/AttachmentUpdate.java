package com.beiming.uhf_test.bean.pic;

import java.util.ArrayList;

/**
 * Created by wanghao on 2017/5/22.
 */

public class AttachmentUpdate {
    private String tag;
    private Object data;//传输的数据
    private ArrayList<PhotoBean> photoBeans;//图片对象集合
    private ArrayList<PhotoBean> deletePhotoBeans;//删除的图片对象集合

    public AttachmentUpdate(String tag, Object data, ArrayList<PhotoBean> photoBeans, ArrayList<PhotoBean> deletePhotoBeans) {
        this.tag = tag;
        this.data = data;
        this.photoBeans = photoBeans;
        this.deletePhotoBeans = deletePhotoBeans;
    }

    public AttachmentUpdate(String t, ArrayList<PhotoBean> attachments) {
        this.tag = t;
        this.photoBeans = attachments;
    }

    public AttachmentUpdate() {
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public ArrayList<PhotoBean> getPhotoBeans() {
        return photoBeans;
    }

    public void setPhotoBeans(ArrayList<PhotoBean> photoBeans) {
        this.photoBeans = photoBeans;
    }

    public ArrayList<PhotoBean> getDeletePhotoBeans() {
        return deletePhotoBeans;
    }

    public void setDeletePhotoBeans(ArrayList<PhotoBean> deletePhotoBeans) {
        this.deletePhotoBeans = deletePhotoBeans;
    }
}
