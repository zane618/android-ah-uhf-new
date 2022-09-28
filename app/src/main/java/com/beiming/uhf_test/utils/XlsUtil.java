package com.beiming.uhf_test.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * excel操作
 */
public class XlsUtil {

    /**
     * 读取excel 第一列 组成list
     * @param excelPath
     * @param list
     */
    public static void readXls(String excelPath, List<String> list) {
        try {
            FileInputStream excelFile = new FileInputStream(excelPath);
            Workbook workbook = Workbook.getWorkbook(excelFile);

            Sheet sheet = workbook.getSheet(0);
            int colCount = sheet.getColumns(); //列数
            int rowCount = sheet.getRows(); //行数
            for (int i = 0; i < rowCount; i++) {

                for (int j = 0; j < colCount; j++) {

                    Cell cell = sheet.getCell(j, i);
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
                            list.add(temp2);
                            break;
                    }
                }
            }
            workbook.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
