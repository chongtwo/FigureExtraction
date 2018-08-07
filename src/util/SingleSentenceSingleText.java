package util;

import java.util.ArrayList;

public class SingleSentenceSingleText {

	public static void main(String[] args) {
		String path = "D:\\JavaApp\\util.FigureExtraction\\out\\LongSentenceTypeCount2018-05-31-16-51-51.txt";
		ArrayList<String> text = TxtOperator.readTxt(path);
		String outLocation = "D:\\JavaApp\\util.FigureExtraction\\out\\SelectedSentence\\";
		int num = 0;
		int end = text.size();

		//产生n个随机数，并选出句子
		int n = 500;
        RandomNum rdmn = new RandomNum();
        ArrayList<Integer> rdmnArrL = rdmn.genRanNum(n,text.size()-1);
        for (int i : rdmnArrL){
			String textName = "impression"+ String.format("%04d", i)+ ".txt";
			String annName = "impression" + String.format("%04d", i) + ".ann";
			String fullPath = outLocation + textName;
			String annfullPath = outLocation + annName;
			TxtOperator.writeTxt(fullPath, text.get(i).split("\t")[0]);
			TxtOperator.writeTxt(annfullPath,"");
			num++;
        }
	}

}
