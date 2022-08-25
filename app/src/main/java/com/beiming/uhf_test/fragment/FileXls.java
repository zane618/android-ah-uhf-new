package com.beiming.uhf_test.fragment;

import android.text.TextUtils;
import android.util.Xml;

import com.beiming.uhf_test.App;
import com.beiming.uhf_test.bean.MeasBoxBean;
import com.beiming.uhf_test.bean.MeterBean;
import com.beiming.uhf_test.bean.pic.PhotoBean;
import com.beiming.uhf_test.utils.Contant;

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
                for (int colNumber = 0; colNumber < Contant.tableTitleList.size(); colNumber++) {
                    content = "";
                    switch (colNumber) {
                        case 0:
                            if (!TextUtils.isEmpty(measBoxBean.getBarCode()))
                                content = measBoxBean.getBarCode();
                            break;
                        case 1:
                            if (!TextUtils.isEmpty(measBoxBean.getMeasAssetNo()))
                                content = measBoxBean.getMeasAssetNo();
                            break;
                        case 2:
                            if (!TextUtils.isEmpty(measBoxBean.getScanTime()))
                                content = measBoxBean.getScanTime();
                            break;
                        case 3:
                            if (!TextUtils.isEmpty(measBoxBean.getGps_X()))
                                content = measBoxBean.getGps_X();
                            break;
                        case 4:
                            if (!TextUtils.isEmpty(measBoxBean.getGps_Y()))
                                content = measBoxBean.getGps_Y();
                            break;
                        case 5:
                            if (!TextUtils.isEmpty(measBoxBean.getGps_Z()))
                                content = measBoxBean.getGps_Z();
                            break;
                        case 6:
                            if (!TextUtils.isEmpty(measBoxBean.getInstAddr()))
                                content = measBoxBean.getInstAddr();
                            break;
                        case 7:
                            if (!TextUtils.isEmpty(measBoxBean.getNote()))
                                content = measBoxBean.getNote();
                            break;
                        case 8://表条码集
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
                        case 9://表资产编号集
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
                        case 10://计量箱图片名称集
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
                        case 11://登录账号
                            if (App.loginBean != null)
                                content = App.loginBean.getUserName();
                            break;
                        case 12://密码
                            if (App.loginBean != null)
                                content = App.loginBean.getPassword();
                            /*String generate = MD5Utils.createActionCode(SerialNumber);//对序列号进行第一次加密
                            LogUtils.i("第一次加密后=" + generate);
                            String generate1 = MD5Utils.generate(generate);//对加密过后的序列号再一次加密*/
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
                            break;
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

    //调试状态成功或失败的状态码
    public static List<String> tableTitleList = new ArrayList<>();
    {
        tableTitleList.add("电表箱条形码编号");
        tableTitleList.add("电表箱资产编号");
        tableTitleList.add("scanTime");
        tableTitleList.add("GPS经度");
        tableTitleList.add("GPS纬度");
        tableTitleList.add("GPS海拔");
        tableTitleList.add("安装地址");
        tableTitleList.add("备注");
        tableTitleList.add("电能表条形码");
        tableTitleList.add("电能表资产编号");
        tableTitleList.add("计量箱图片名称");
    }
}
