import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;


public class XlsOperator {
    String string;
    int rowIndex;
    Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet();

    public static void main(String[] args) throws IOException {
        String path = "C:\\\\Users\\\\W\\\\Desktop\\\\新建 Microsoft Excel 97-2003 工作表.xls";
//        String path = "C:/Users/W/Desktop/新建 Microsoft Excel 工作表.xlsx";
        XlsOperator test = new XlsOperator();
//        HashMap<Integer, ArrayList<String>> xlsRead = XlsOperator.readXls(path);
//        System.out.println(xlsRead);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("a");
        arrayList.add("b");
        arrayList.add("c");
        test.writeXls(path, arrayList);
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
    public void writeXls(String path, ArrayList<ArrayList<String>> list){

        System.out.println("开始写入文件>>>>>>>>>>>");
        try {
            for (int i = 0; i < list.size();i++ ){
                Row row = sheet.createRow(i+ rowIndex);
                for (int j = 0; j <list.get(i).size(); j++){
                    Cell cell = row.createCell(j);
                    cell.setCellValue(list.get(i).get(j));
                }
            }
            FileOutputStream fos = new FileOutputStream(path);
            workbook.write(fos);
            fos.close();
            System.out.println("主表数据写入完成>>>>>");
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void writeXls(String path, List list){

        System.out.println("开始写入文件>>>>>>>>>>>");
        try {
            Row row = sheet.createRow(rowIndex);
            //将list内容按一行多列写入
            for ( int i = 0; i < list.size(); i ++){
                Cell cell = row.createCell(i);
                cell.setCellValue((String)list.get(i));
            }
            FileOutputStream fos = new FileOutputStream(path);
            workbook.write(fos);
            fos.close();
            System.out.println("主表数据写入完成>>>>>");
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        rowIndex++;
    }


    /**
     * 写xlsx文件
     * @param path
     */
    public static void writeXlsx(String path){

    }
}
