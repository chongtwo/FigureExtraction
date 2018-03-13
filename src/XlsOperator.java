import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class XlsOperator {
//    public static void main(String[] args){
//        XlsOperator test = new XlsOperator();
//        String xlsxText = test.readXlsx("C:\\Users\\W\\Desktop\\新建 Microsoft Excel 工作表.xlsx");
//        System.out.println(xlsxText);
//    }

    /**
     * 读xls
     * @param path
     * @return
     */
    public static HashMap<String, String> readXls(String path)
    {
        HashMap<String, String> map = new HashMap<String, String>();
        try
        {
            FileInputStream is =  new FileInputStream(path);
            HSSFWorkbook excel=new HSSFWorkbook(is);
            //获取第一个sheet  
            HSSFSheet sheet0=excel.getSheetAt(0);
            //行读取循环
            for (Iterator rowIterator = sheet0.iterator(); rowIterator.hasNext();)
            {
                HSSFRow row=(HSSFRow) rowIterator.next();
                String key = "";
                String value = "";
                //列读取循环
                for (Iterator iterator=row.cellIterator();iterator.hasNext();)
                {
                    HSSFCell cell=(HSSFCell) iterator.next();
                    if (cell.getColumnIndex() == 0){
                        key = cell.getStringCellValue();
                    }
                    if(cell.getColumnIndex() == 1){
                        value = cell.getStringCellValue();
                    }
                }
                map.put(key, value);
            }
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block  
            e.printStackTrace();
            //log.warn(e);
        }
//        for (Map.Entry<String, String> entry : map.entrySet()){
//            System.out.println(entry.getKey() + " : " + entry.getValue());
//        }
        return map;

    }

    /**
     * 将List集合数据写入xls文件
     * @param path
     */
    public static void writeXls(String path, List list){
        System.out.println("开始写入文件>>>>>>>>>>>");
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        String[] excelTitle = new String[10]; //用于存放表头
        int rowIndex = 0;
        try {
            //写表头数据
            Row titleRow = sheet.createRow(rowIndex);
            for (int i = 0; i < excelTitle.length; i++) {
                //创建表头单元格，填值
                titleRow.createCell(i).setCellValue(excelTitle[i]);
            }
            System.out.println("表头写入完成>>>>>>>");
            rowIndex++;
            //循环写入主表数据
            for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
                Object listElement = iterator.next();
                //create sheet row
                Row row = sheet.createRow(rowIndex);
                //create sheet column
                Cell cell0 = row.createCell(0);
                cell0.setCellValue("a");
                rowIndex++;
            }
            System.out.println("主表数据写入完成>>>>>");
            FileOutputStream fos = new FileOutputStream(path);
            workbook.write(fos);
            fos.close();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    /**
     * 读xlsx
     * @param path
     * @return
     */
    public static String readXlsx(String path)
    {
        String text="";
        try
        {
            OPCPackage pkg=OPCPackage.open(path);
            XSSFWorkbook excel=new XSSFWorkbook(pkg);
            //获取第一个sheet
            XSSFSheet sheet0=excel.getSheetAt(0);
            for (Iterator rowIterator=sheet0.iterator();rowIterator.hasNext();)
            {
                XSSFRow row=(XSSFRow) rowIterator.next();
                for (Iterator iterator=row.cellIterator();iterator.hasNext();)
                {
                    XSSFCell cell=(XSSFCell) iterator.next();
                    //根据单元的的类型 读取相应的结果
                    if(cell.getCellType()==XSSFCell.CELL_TYPE_STRING) text+=cell.getStringCellValue()+"\t";
                    else if(cell.getCellType()==XSSFCell.CELL_TYPE_NUMERIC) text+=cell.getNumericCellValue()+"\t";
                    else if(cell.getCellType()==XSSFCell.CELL_TYPE_FORMULA) text+=cell.getCellFormula()+"\t";
                }
                text+="\n";
            }
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //log.warn(e);
        }

        return text;
    }

    /**
     * 写xlsx文件
     * @param path
     */
    public static void writeXlsx(String path){

    }
}
