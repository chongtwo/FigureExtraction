import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SentenceTypeCount {

//    static ArrayList<String> distinctSentenceList = new ArrayList<>();
    static HashMap<String, Integer> countSentenceList = new HashMap<>();

    public static void main(String[] args) {
        SentenceTypeCount sentenceTypeCount = new SentenceTypeCount();
        String readPath = ".\\out\\SentenceTypeCount2018-05-07-11-31-03.txt";
        String outPath = ".\\out\\YingSentenceTypeCount";
        sentenceTypeCount.count(readPath, outPath);
    }

    public void count(String readPath, String outPath){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        Date date = new Date();
        outPath = outPath + String.valueOf(dateFormat.format(date))+".txt";
        ArrayList<String> longSentenceList = TxtOperator.readTxt(readPath);

        String content;
        String[] punctuationList = {"。", "，", "；",",", ";"};

        for (String longSentence : longSentenceList){
            LongSentence ls = new LongSentence(longSentence);
            ls.segToShort(punctuationList);
            for (ShortSentence ss : ls.getShortSentences()){
                if (longSentence.contains("影")) {
//              ShortSentence ss = new ShortSentence();
                    ss.content = longSentence.split("\t")[0];
                    int num;
                    //判断最后一个字符是否是标点，是就去掉，不是则保留
                    if (ss.content.endsWith(",") | ss.content.endsWith("，") | ss.content.endsWith("。") | ss.content.endsWith("；") | ss.content.endsWith(";")) {
                        content = ss.content.substring(0, ss.content.length() - 1);//去除句末标点，以免只因句末标点不同而影响匹配
                    } else {
                        content = ss.content;
                    }
                    boolean isContain = countSentenceList.containsKey(content);
                    try {
                        num = countSentenceList.get(content);
                    } catch (Exception e) {
                        num = 1;
                    }
                    if (isContain) {
                        num++;
                    }
                    countSentenceList.put(content, num); //会覆盖已有的key和value
                }
            }
        }
        Map<String, Integer> resultMap = new MapSort().sortMapByValue(countSentenceList); //按Value进行排序

        WriteList.writeList(outPath, resultMap);
    }
    }
