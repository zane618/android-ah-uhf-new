package com.beiming.uhf_test.fragment;

import android.text.TextUtils;
import android.util.Xml;

import com.beiming.uhf_test.App;
import com.beiming.uhf_test.bean.LibAssetBean;
import com.beiming.uhf_test.bean.MeasBoxBean;
import com.beiming.uhf_test.bean.MeterBean;
import com.beiming.uhf_test.bean.pic.PhotoBean;
import com.beiming.uhf_test.db.GreenDaoManager;
import com.beiming.uhf_test.utils.Contant;
import com.beiming.uhf_test.utils.FileOperator;
import com.beiming.uhf_test.utils.LogPrintUtil;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * Created by Administrator on 2018/7/25.
 */

public class FileXls {

    private static final int DEFAULT_SHEET = 0;

    //读取数据转化成LibAssetBean 集合
    public static List<LibAssetBean> readXLSToLibAssetList(String path, int startRows) {
        List<LibAssetBean> list = new ArrayList<>();
        final int MAX_ROWS = 4000;
        try {
            Workbook workbook = Workbook.getWorkbook(new File(path));
            Sheet sheet = workbook.getSheet(0);
            int columnCount = sheet.getColumns();

            int rowCount = sheet.getRows();
            int exeRowCount = rowCount;
            if (rowCount > startRows + MAX_ROWS) {
                exeRowCount = startRows + MAX_ROWS;
            }

            Cell cell = null;
            for (int i = startRows; i < exeRowCount; i++) {
                LibAssetBean asset = new LibAssetBean();
                for (int j = 0; j < columnCount; j++) {
                    cell = sheet.getCell(j, i);
                    String temp2 = "";
                    if (cell.getType() == CellType.NUMBER) {
                        temp2 = ((NumberCell) cell).getValue() + "";
                    } else if (cell.getType() == CellType.DATE) {
                        temp2 = "" + ((DateCell) cell).getDate();
                    } else {
                        temp2 = "" + cell.getContents();
                    }

                    switch (j) {
                        case 0:
                            LogPrintUtil.zhangshi("temp2:" + temp2);
                            break;
                        case 1:
                            asset.setDanweiCode(temp2);
                            break;
                        case 2:
                            asset.setDanwei(temp2);
                            break;
                        case 3:
                            asset.setKufangCode(temp2);
                            break;
                        case 4:
                            asset.setKufang(temp2);
                            break;
                        case 5:
                            asset.setKuquCode(temp2);
                            break;
                        case 6:
                            asset.setKuqu(temp2);
                            break;
                        case 7:
                            asset.setAssetNo(temp2);
                            break;
                        case 8:
                            asset.setStateCode(temp2);
                            break;
                        case 9:
                            asset.setState(temp2);
                            break;
                    }
                }
                list.add(asset);
            }
            workbook.close();
            GreenDaoManager.getInstance().getSession().getLibAssetBeanDao().insertInTx(list);

            if (exeRowCount < rowCount) {
                readXLSToLibAssetList(path, exeRowCount);
            }
        } catch (Exception e) {
            return null;
        }

        return list;
    }

    public static String readXLS(String path) {
        String str = "";

        try {
            Workbook workbook = Workbook.getWorkbook(new File(path));
            Sheet sheet = workbook.getSheet(0);
            int columnCount = sheet.getColumns();
            int rowCount = sheet.getRows();

            Cell cell = null;
            for (int i = 0; i < rowCount; i++) {
                for (int j = 0; j < columnCount; j++) {
                    cell = sheet.getCell(j, i);
                    String temp2 = "";
                    if (cell.getType() == CellType.NUMBER) {
                        temp2 = ((NumberCell) cell).getValue() + "";
                    } else if (cell.getType() == CellType.DATE) {
                        temp2 = "" + ((DateCell) cell).getDate();
                    } else {
                        temp2 = "" + cell.getContents();
                    }
                    str = str + "  " + temp2;
                }
                str += "\n";
            }
            workbook.close();
        } catch (Exception e) {
        }

        return str;
    }

    public static ArrayList<HashMap<String, Object>> readXLSmap(String path) {
        String str = "";
        ArrayList<HashMap<String, Object>> listmap = new ArrayList<HashMap<String, Object>>();
        try {
            Workbook workbook = Workbook.getWorkbook(new File(path));
            Sheet sheet = workbook.getSheet(0);
            int columnCount = sheet.getColumns();
            int rowCount = sheet.getRows();

            Cell cell = null;
            for (int i = 1; i < rowCount; i++) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("tagUii", sheet.getCell(0, i).getContents());
                //  map.put("jl", sheet.getCell(1, i).getContents());


                listmap.add(map);

            }
            workbook.close();
        } catch (Exception e) {
        }

        return listmap;
    }

    public static List<ArrayList<String>> readXLSX(String path) {
        String v = null;
        List<String> list = new ArrayList<String>();
        List<ArrayList<String>> table = new ArrayList<ArrayList<String>>();

        try {
            ZipFile file = new ZipFile(new File(path));

            ZipEntry sharedStringXML = file.getEntry("xl/sharedStrings.xml");
            InputStream inputStream = file.getInputStream(sharedStringXML);
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(inputStream, "utf-8");
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_TAG:
                        String tag = parser.getName();
                        if (tag.equalsIgnoreCase("t")) {
                            list.add(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                    default:
                        break;
                }

                event = parser.next();
            }

            ZipEntry sheetXML = file.getEntry("xl/worksheets/sheet1.xml");
            InputStream inputStreamsheet = file.getInputStream(sheetXML);
            parser = Xml.newPullParser();
            parser.setInput(inputStreamsheet, "utf-8");
            event = parser.getEventType();

            ArrayList<String> row = null;
            boolean isText = false;
            while (event != XmlPullParser.END_DOCUMENT) {

                switch (event) {
                    case XmlPullParser.START_TAG:
                        String tag = parser.getName();

                        if (tag.equalsIgnoreCase("row")) {
                            row = new ArrayList<String>();
                            table.add(row);
                        } else if (tag.equalsIgnoreCase("c")) {
                            String t = parser.getAttributeValue(null, "t");

                            if (t != null) {
                                isText = true;
                            } else {
                                isText = false;
                            }
                        } else if (tag.equalsIgnoreCase("v")) {
                            String cell = parser.nextText();

                            if (cell != null) {
                                if (isText) {
                                    row.add(list.get(Integer.parseInt(cell)));
                                } else {
                                    row.add(cell);
                                }
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("row") && v != null) {
                        }
                        break;
                }

                event = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return table;
    }

    public static boolean writeXLS(String path, List<MeasBoxBean> table, boolean isTableTitle) {
        File file = createXLS(path);

        if (file == null) {
            return false;
        } else {

            try {
                return addData(file, table, isTableTitle);
            } catch (RowsExceededException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return false;

            } catch (WriteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return false;
            }

        }

    }

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

    private static boolean addData(File file, List<MeasBoxBean> table, boolean isTableTitle)
            throws IOException, RowsExceededException, WriteException {

        Workbook wb = null;
        try {
            wb = Workbook.getWorkbook(file);
        } catch (BiffException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        WritableWorkbook book = Workbook.createWorkbook(file, wb);
        WritableSheet sheet = book.getSheet(0);
        // List<String> attrs = table;
        int i = sheet.getRows();
        int rowNumber = 0;
        String content = "";
        //判断是写表头还是，内容
        if (isTableTitle) {
            //表头
            //i+j是行号
            for (int k = 0; k < Contant.tableTitleList.size(); k++) {
                Label label = new Label(k, rowNumber, Contant.tableTitleList.get(k));
                sheet.addCell(label);
            }
        } else {
            //内容
            for (int j = 0; j < table.size(); j++) {

                rowNumber = i + j;
                MeasBoxBean measBoxBean = (MeasBoxBean) table.get(j);
                //处理图片
//                LogPrintUtil.zhangshi("file.getParent():" + file.getParent());
//                List<PhotoBean> photoBeanList = measBoxBean.getBoxImages();
//                for (PhotoBean photoBean : photoBeanList) {
//                    FileOperator.moveFile(photoBean.getImageSrc(), file.getParent());
//                }


                for (int colNumber = 0; colNumber < Contant.tableTitleList.size(); colNumber++) {
                    content = "";
                    switch (Contant.tableTitleList.get(colNumber)) {
                        case "电表箱条形码编号":
                            if (!TextUtils.isEmpty(measBoxBean.getBarCode()))
                                content = measBoxBean.getBarCode();
                            break;
                        case "电表箱资产编号":
                            if (!TextUtils.isEmpty(measBoxBean.getMeasAssetNo()))
                                content = measBoxBean.getMeasAssetNo();
                            break;
                        case "采集时间":
                            if (!TextUtils.isEmpty(measBoxBean.getScanTime()))
                                content = measBoxBean.getScanTime();
                            break;
                        case "GPS经度":
                            if (!TextUtils.isEmpty(measBoxBean.getGps_X()))
                                content = measBoxBean.getGps_X();
                            break;
                        case "GPS纬度":
                            if (!TextUtils.isEmpty(measBoxBean.getGps_Y()))
                                content = measBoxBean.getGps_Y();
                            break;
                        case "GPS海拔":
                            if (!TextUtils.isEmpty(measBoxBean.getGps_Z()))
                                content = measBoxBean.getGps_Z();
                            break;
                        case "安装地址":
                            if (!TextUtils.isEmpty(measBoxBean.getInstAddr()))
                                content = measBoxBean.getInstAddr();
                            break;
                        case "备注":
                            if (!TextUtils.isEmpty(measBoxBean.getNote()))
                                content = measBoxBean.getNote();
                            break;
                        case "电能表条形码"://表条码集
                            if (measBoxBean.getMeters() != null && measBoxBean.getMeters().size() > 0) {
                                for (MeterBean meterBean : measBoxBean.getMeters()) {
                                    if (TextUtils.isEmpty(content)) {
                                        content = meterBean.getBarCode();
                                    } else {
                                        content = content + "/" + meterBean.getBarCode();
                                    }
                                }
                            }
                            break;
                        case "电能表资产编号"://表资产编号集
                            if (measBoxBean.getMeters() != null && measBoxBean.getMeters().size() > 0) {
                                for (MeterBean meterBean : measBoxBean.getMeters()) {
                                    if (TextUtils.isEmpty(content)) {
                                        content = meterBean.getMeterAssetNo();
                                    } else {
                                        content = content + "/" + meterBean.getMeterAssetNo();
                                    }
                                }
                            }
                            break;
                        case "计量箱图片名称"://计量箱图片名称集
                            if (measBoxBean.getBoxImages() != null && measBoxBean.getBoxImages().size() > 0) {
                                for (PhotoBean photoBean : measBoxBean.getBoxImages()) {
                                    if (TextUtils.isEmpty(content)) {
                                        content = photoBean.getImageSrc();
                                    } else {
                                        content = content + "/" + photoBean.getImageSrc();
                                    }
                                }
                            }
                            break;


                        case "材质"://材质
                            if (!TextUtils.isEmpty(measBoxBean.getCaizhi()))
                                content = measBoxBean.getCaizhi();
                            break;
                        case "电表箱高度"://电表箱高度
                            if (!TextUtils.isEmpty(measBoxBean.getGao()))
                                content = measBoxBean.getGao();
                            break;
                        case "电表箱宽度"://电表箱宽度
                            if (!TextUtils.isEmpty(measBoxBean.getKuan()))
                                content = measBoxBean.getKuan();
                            break;
                        case "电表箱深度"://电表箱深度
                            if (!TextUtils.isEmpty(measBoxBean.getShen()))
                                content = measBoxBean.getShen();
                            break;
                        case "缺陷等级"://缺陷等级
                            if (!TextUtils.isEmpty(measBoxBean.getHasQx()))
                                content = measBoxBean.getHasQx();
                            break;
                        case "缺陷详情"://缺陷详情
                            if (!TextUtils.isEmpty(measBoxBean.getQxDetail()))
                                content = measBoxBean.getQxDetail();
                            break;
                        case "分支箱条形码编号"://分支箱条形码编号
                            if (!TextUtils.isEmpty(measBoxBean.getFenzhixCode()))
                                content = measBoxBean.getFenzhixCode();
                            break;
                        case "分支箱资产编号"://分支箱资产编号
                            if (!TextUtils.isEmpty(measBoxBean.getFenzhixCode()))
                                content = measBoxBean.getFenzhixAssetNo();
                            break;
                        case "行"://行数
                            if (!TextUtils.isEmpty(measBoxBean.getBoxRows()))
                                content = measBoxBean.getBoxRows();
                            break;
                        case "列"://列
                            if (!TextUtils.isEmpty(measBoxBean.getBoxCols()))
                                content = measBoxBean.getBoxCols();
                            break;

                        case "左上门高度":
                            if (!TextUtils.isEmpty(measBoxBean.getZsGao()))
                                content = measBoxBean.getZsGao();
                            break;
                        case "左上门宽度":
                            if (!TextUtils.isEmpty(measBoxBean.getZsKuan()))
                                content = measBoxBean.getZsKuan();
                            break;
                        case "左下门高度":
                            if (!TextUtils.isEmpty(measBoxBean.getZxGao()))
                                content = measBoxBean.getZxGao();
                            break;
                        case "左下门宽度":
                            if (!TextUtils.isEmpty(measBoxBean.getZxKuan()))
                                content = measBoxBean.getZxKuan();
                            break;
                        case "右上门高度":
                            if (!TextUtils.isEmpty(measBoxBean.getYsGao()))
                                content = measBoxBean.getYsGao();
                            break;
                        case "右上门宽度":
                            if (!TextUtils.isEmpty(measBoxBean.getYsKuan()))
                                content = measBoxBean.getYsKuan();
                            break;
                        case "右下门高度":
                            if (!TextUtils.isEmpty(measBoxBean.getYxGao()))
                                content = measBoxBean.getYxGao();
                            break;
                        case "右下门宽度":
                            if (!TextUtils.isEmpty(measBoxBean.getYxKuan()))
                                content = measBoxBean.getYxKuan();
                            break;



                        /*case 11://登录账号
                            if (App.loginBean != null)
                                content = App.loginBean.getUserName();
                            break;
                        case 12://密码
                            if (App.loginBean != null)
                                content = App.loginBean.getPassword();
                            *//*String generate = MD5Utils.createActionCode(SerialNumber);//对序列号进行第一次加密
                            LogUtils.i("第一次加密后=" + generate);
                            String generate1 = MD5Utils.generate(generate);//对加密过后的序列号再一次加密*//*
                            break;
                        case 13://所在项目组
                            if (App.loginBean != null)
                                content = App.loginBean.getProjectTeam();
                            break;
                        case 14://答案一
                            if (App.loginBean != null)
                                content = App.loginBean.getAnswerOne();
                            break;
                        case 15://答案二
                            if (App.loginBean != null)
                                content = App.loginBean.getAnswerTwo();
                            break;
                        case 16://答案三
                            if (App.loginBean != null)
                                content = App.loginBean.getAnswerThree();
                            break;*/
                    }
                    //i+j是行号
                    Label label = new Label(colNumber, rowNumber, content);
                    sheet.addCell(label);
                }
            }

        }
        /*
         * for (int attr = 0; attr < attrs.size(); attr++) { Label label = new
         * Label(attr, i, attrs.get(attr)); sheet.addCell(label); }
         */

        book.write();
        book.close();
        wb.close();
        return true;

    }

    public static final int CREATE_FAIL = -1;
    public static final int ADD_DATA_FAIL = -2;
}
