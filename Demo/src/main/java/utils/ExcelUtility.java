package utils;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtility {
    public static FileInputStream file;
    public static FileOutputStream fileOut;
    public static XSSFWorkbook wb;
    public static XSSFSheet ws;
    public static XSSFRow row;
    public static XSSFCell cell;


    public static int getRowCount(String excelFile, String excelSheet) throws IOException {
        /* Returns row count in excel file */

        file = new FileInputStream(excelFile);
        wb = new XSSFWorkbook(file);

        ws = wb.getSheet(excelSheet);

        int rowCount = ws.getLastRowNum();
        //wb.close();
        file.close();

        return rowCount;

    }
    public static int getCellCount(String excelFile, String excelSheet, int rowNum) throws IOException {
        /* Returns cell count in excel file */
        file = new FileInputStream(excelFile);
        wb = new XSSFWorkbook(file);
        ws = wb.getSheet(excelSheet);
        row = ws.getRow(rowNum);
        int cellCount = row.getLastCellNum();
        //wb.close();
        file.close();
        return cellCount;

    }
    public static String getCellData(String excelFile, String excelSheet, int rownum, int colnum) throws IOException {

        /* Gets data in specific cell */

        file = new FileInputStream(excelFile);
        wb = new XSSFWorkbook(file);

        ws = wb.getSheet(excelSheet);

        row = ws.getRow(rownum);
        cell = row.getCell(colnum);

        String data;
        try {
            DataFormatter formatter = new DataFormatter();
            String cellData = formatter.formatCellValue(cell);
            return cellData;
        } catch (Exception e) {
            data = "";
        }
        //wb.close();
        file.close();
        return data;
    }


}
