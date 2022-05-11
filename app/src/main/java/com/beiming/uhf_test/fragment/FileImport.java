package com.beiming.uhf_test.fragment;

import android.util.Log;

import com.beiming.uhf_test.bean.MeasBoxBean;
import com.beiming.uhf_test.utils.ConstantUtil;
import com.beiming.uhf_test.utils.TimeUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/7/25.
 */

public class FileImport {
    private static String TAG = "FileImport";

    public static boolean daochu(String tmpname, List<MeasBoxBean> measBoxBeans) {

        try {

            String file = "";
            String packgePath = ConstantUtil.EXCEL_STR + TimeUtils.getY_M_D_Time() + "/";
            if (tmpname.isEmpty())
                file = ConstantUtil.EXCEL_STR + TimeUtils.getY_M_D_Time() + "/"
                        + TimeUtils.getTime() + ".xls";
            else
                file = ConstantUtil.EXCEL_STR + tmpname;

            File packge = new File(packgePath);

            if (!packge.exists()) {
                packge.mkdirs();
                Log.i(TAG, "文件夹不存在");
            } else {
                Log.i(TAG, "文件夹存在");
            }

            //写入表头
            FileXls.writeXLS(file, null, true);

/*            List<Object> ac = new ArrayList<Object>();

            String id = "";
            // String sxl = "";
            for (int i = 0; i < lists2.size(); i++) {
                List<String> al = new ArrayList<String>();
                Set<Entry<String, String>> sets = lists2.get(i).entrySet();


                for (Entry<String, String> entry : sets) {

                    if (entry.getKey().equals("tagUii")) {
                        id = entry.getValue().toString();
                    } else {
                    }
                    // Object value=entry.getValue();
                }
                al.add(id);

                // al.add(sxl);
                ac.add(al);
            }*/

            return FileXls.writeXLS(file, measBoxBeans, false);
        } catch (Exception ex) {
            Log.i("导出异常", ex.getMessage());
            return false;
        }
    }


    public static String GetTimesyyyymmdd() {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String dt = formatter.format(curDate);

        return dt;

    }

    public static String GetTimesddMMyy() {

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String dt = formatter.format(curDate);

        return dt;

    }

    public static String GetTimesyyyymmddhhmmss() {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String dt = formatter.format(curDate);

        return dt;

    }
}
