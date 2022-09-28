package com.beiming.uhf_test.library;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.beiming.uhf_test.bean.LibAssetBean;
import com.beiming.uhf_test.bean.MeasBoxBean;
import com.beiming.uhf_test.fragment.FileImport;
import com.beiming.uhf_test.fragment.FileXls;
import com.beiming.uhf_test.tools.UIHelper;
import com.beiming.uhf_test.utils.ConstantUtil;
import com.beiming.uhf_test.utils.Contant;
import com.beiming.uhf_test.utils.LogPrintUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 * 导出盘点数据
 */
public class DcLibAssets {

    private Context mContext;

    public DcLibAssets(Context context) {
        this.mContext = context;
    }

    public static boolean daochu(String fileDirString, String fileName, List<LibAssetBean> libAssetBeans) {

        try {
            String absoluteFilePathString = ConstantUtil.LIB_EXCEL_STR + fileDirString + "/";
            File absoluteFileDir = new File(absoluteFilePathString);
            if (!absoluteFileDir.exists()) {
                absoluteFileDir.mkdirs();
                LogPrintUtil.zhangshi("文件夹不存在");
            } else {
                LogPrintUtil.zhangshi("文件夹存在");
            }
            String file = absoluteFilePathString + fileName;
            //写入数据
            return writeXLS(file, libAssetBeans);
        } catch (Exception ex) {
            Log.i("导出异常", ex.getMessage());
            return false;
        }
    }

    private static boolean writeXLS(String path, List<LibAssetBean> libAssetBeans) {
        File file = createXLS(path);
        if (file == null) {
            return false;
        }
        Workbook wb = null;
        WritableWorkbook book = null;
        try {
            wb = Workbook.getWorkbook(file);
            book = Workbook.createWorkbook(file, wb);
        } catch (IOException | BiffException e) {
            e.printStackTrace();
        }
        if (book != null) {
            WritableSheet sheet = book.getSheet(0);
            int i = sheet.getRows();
            int rowNumber = 0;
            String content = "";
            //写表头
            //i+j 是行号
            for (int k = 0; k < Contant.libTableTitleList.size(); k++) {
                Label label = new Label(k, rowNumber, Contant.libTableTitleList.get(k));
                try {
                    sheet.addCell(label);
                } catch (WriteException e) {
                    e.printStackTrace();
                }
            }
            i++;
            //写内容
            for (int j = 0; j < libAssetBeans.size(); j++) {
                rowNumber = i + j;
                LibAssetBean bean = libAssetBeans.get(j);

                for (int colNumber = 0; colNumber < Contant.libTableTitleList.size(); colNumber++) {
                    switch (Contant.libTableTitleList.get(colNumber)) {
                        case "资产编号":
                            if (!TextUtils.isEmpty(bean.getAssetNo())) {
                                content = bean.getAssetNo();
                            }
                            break;
                    }
                    //i+j是行号
                    Label label = new Label(colNumber, rowNumber, content);
                    try {
                        sheet.addCell(label);
                    } catch (WriteException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        try {
            book.write();
            book.close();
            wb.close();
        } catch (IOException | WriteException e) {
            e.printStackTrace();
        }
        return true;
    }


    /**
     * 创建xls
     */
    private static File createXLS(String path) {
        File file = null;
        try {
            file = new File(path);
            if (file.exists())
                return file;
            WritableWorkbook book = Workbook.createWorkbook(file);
            WritableSheet sheet = book.createSheet("sheet1", 0);

            book.write();
            book.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return file;
        }
    }

}
