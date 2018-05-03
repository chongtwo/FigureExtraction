import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SentenceTypeCount {

    static ArrayList<String> distinctSentenceList = new ArrayList<>();

    public void count(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        Date date = new Date();
        String readPath = ".\\static\\CT胸部平扫约4000份-描述.txt";
        ArrayList<String> longSentenceList = TxtOperator.readTxt(readPath);
        String outPath = ".\\out\\SentenceTypeCount"+String.valueOf(dateFormat.format(date))+".txt";

        for (String longSentence : longSentenceList){
            LongSentence ls = new LongSentence(longSentence);
            ls.segToShort();
            for (ShortSentence ss : ls.shortSentences){
                boolean uniqueFlag = SpecialChar.isUnique(ss.content, distinctSentenceList);
                if(uniqueFlag){
                    distinctSentenceList.add(ss.content);
                }
            }
        }
        WriteList.writeList(outPath, distinctSentenceList);
    }

    public static void main(String[] args) {
        SentenceTypeCount sentenceTypeCount = new SentenceTypeCount();
        sentenceTypeCount.count();
    }

}
