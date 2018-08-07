package LHM;

import util.TxtOperator;
import util.XlsOperator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class SingleReportSingleTxt {

	public static void main(String[] args) {
		String excelPath = "D:\\1.放射报告结构化\\李昊旻老师\\heartus4extract.xls";
		String txtOutPath = "D:\\JavaApp\\FigureExtraction\\out\\LHM\\";
		int sheetIndex = 0;
		int columnIndex = 3;
		try {
			HashMap<String, String> reportMap = XlsOperator.readXlsWithRowNum(excelPath, sheetIndex, columnIndex);
			for (String reportRowId: reportMap.keySet()){
				int id = Integer.valueOf(reportRowId);
				TxtOperator.writeTxt(txtOutPath + String.format("%05d", id) + ".txt", reportMap.get(reportRowId));
				System.out.println(reportRowId);
			}
		}catch (RuntimeException e){

		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
