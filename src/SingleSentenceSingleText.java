import java.util.ArrayList;

public class SingleSentenceSingleText {

	public static void main(String[] args) {
		String path = "D:\\JavaApp\\FigureExtraction\\out\\LongSentenceTypeCount2018-05-31-16-51-51.txt";
		ArrayList<String> text = TxtOperator.readTxt(path);
		String outLocation = "D:\\JavaApp\\FigureExtraction\\out\\SingleSentence\\";
		int num = 0;
		int end = text.size();
		for (String sentence : text.subList(1,end)){
			String textName = "impression"+ String.format("%04d", num)+ ".txt";
			String fullPath = outLocation + textName;
			TxtOperator.writeTxt(fullPath, sentence.split("\t")[0]);
			num++;
		}
	}

}
