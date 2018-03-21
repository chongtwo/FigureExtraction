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

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;


public class XlsOperator {

    public static void main(String[] args) throws IOException {
//        String path = "C:\\\\Users\\\\W\\\\Desktop\\\\新建 Microsoft Excel 97-2003 工作表.xls";
        String path = "C:/Users/W/Desktop/新建 Microsoft Excel 工作表.xlsx";
        XlsOperator test = new XlsOperator();
        HashMap<Integer, ArrayList<String>> xlsText = XlsOperator.readXls(path);
//        ArrayList<String> cellValue = test.readXls(path, 0, 0);
//        System.out.println(cellValue);

//        ArrayList<String> xlsText = XlsOperator.readXls("C:\\Users\\W\\Desktop\\新建 Microsoft Excel 97-2003 工作表.xls", 0, columnlist);
        System.out.println(xlsText);
    }

    /**
     * 读xls或xlsx，按行读每一列的值
     * @param path
     * @return HashMap,key = 行号, value = 该行每列值的ArrayList
     */
    public static HashMap<Integer, ArrayList<String>> readXls(String path)
    {
        HashMap<Integer, ArrayList<String>> map = new HashMap<>();
        ArrayList<String> cellValue = new ArrayList<>();
        try
        {
//            File file = new File(path);
            FileInputStream is =  new FileInputStream(path);
            Workbook excel = null;
            if (path.endsWith("xls")){
                excel = new HSSFWorkbook(is);
            }else if(path.endsWith("xlsx"))
            {
                excel = new XSSFWorkbook(is);
            }

            //获取第一个sheet
            Sheet sheet0=excel.getSheetAt(0);
            int numOfRow = 0;
            //行读取循环
            for (Iterator rowIterator = sheet0.iterator(); rowIterator.hasNext();)
            {
                Row row = (Row) rowIterator.next();
                map.put(numOfRow, new ArrayList<>());
                //列读取循环
                for (Iterator iterator = row.cellIterator();iterator.hasNext();)
                {
                    Cell cell=(Cell) iterator.next();
                    map.get(numOfRow).add(cell.getStringCellValue());
                }
                numOfRow++;
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
     * readXls重载，
     * @param sheetIndex
     * @param columnIndex
     * @return
     */
    public ArrayList<String> readXls(String path, int sheetIndex, int columnIndex) throws IOException {
        FileInputStream is = new FileInputStream(path);
        HSSFWorkbook excel = new HSSFWorkbook(is);
        ArrayList<String> list = new ArrayList<>();
        try{
            //获取第sheetIndex个sheet
            HSSFSheet sheet = excel.getSheetAt(sheetIndex);


            //行读取循环
            for (Iterator rowIterator = sheet.iterator(); rowIterator.hasNext();)
            {
                HSSFRow row = (HSSFRow) rowIterator.next();
                //读取固定列
                HSSFCell cell = (HSSFCell) row.getCell(columnIndex);
                list.add(cell.getStringCellValue());

            }
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //log.warn(e);
        }
        return list;

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
     * 写xlsx文件
     * @param path
     */
    public static void writeXlsx(String path){

    }
}
