package com.beiming.uhf_test.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by htj on 2020/7/10.
 */

public class Contant {
    /**
     * 表箱数据
     */
    public static List<String> tableTitleList = new ArrayList<>();

    /**
     * 盘库excel表头
     */
    public static List<String> libTableTitleList = new ArrayList<>();

    static {
        tableTitleList.add("电表箱条形码编号");
        tableTitleList.add("电表箱资产编号");
        tableTitleList.add("采集时间");
        tableTitleList.add("GPS经度");
        tableTitleList.add("GPS纬度");
        tableTitleList.add("GPS海拔");
        tableTitleList.add("安装地址");
        tableTitleList.add("备注");
        tableTitleList.add("电能表条形码");
        tableTitleList.add("电能表资产编号");
        tableTitleList.add("计量箱图片名称");

        tableTitleList.add("材质");
        tableTitleList.add("电表箱高度");
        tableTitleList.add("电表箱宽度");
        tableTitleList.add("电表箱深度");
        tableTitleList.add("缺陷等级");
        tableTitleList.add("缺陷详情");
        tableTitleList.add("分支箱条形码编号");
        tableTitleList.add("分支箱资产编号");
        tableTitleList.add("行");
        tableTitleList.add("列");

        tableTitleList.add("左上门高度");
        tableTitleList.add("左上门宽度");
        tableTitleList.add("左下门高度");
        tableTitleList.add("左下门宽度");
        tableTitleList.add("右上门高度");
        tableTitleList.add("右上门宽度");
        tableTitleList.add("右下门高度");
        tableTitleList.add("右下门宽度");



        /*tableTitleList.add("登录账号");
        tableTitleList.add("登录密码");
        tableTitleList.add("所在项目组");
        tableTitleList.add("答案一");
        tableTitleList.add("答案二");
        tableTitleList.add("答案三");*/

        initLib();
    }

    private static void initLib() {
        libTableTitleList.add("资产编号");
    }



}
