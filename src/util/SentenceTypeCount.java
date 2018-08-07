package util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SentenceTypeCount {
    static String[] COUNT_REPORT = {};
    static String[] COUNT_LONG_SENTENCE = {"。"};
    static String[] COUNT_SHORT_SENTENCE = {"。", "，", "；",",", ";"};

//    static ArrayList<String> distinctSentenceList = new ArrayList<>();
    static HashMap<String, Integer> countSentenceList = new HashMap<>();

    public static void main(String[] args) {
        SentenceTypeCount sentenceTypeCount = new SentenceTypeCount();
        String readPath = ".\\static\\梧州胸部影像报告20180625-描述.txt";
        String outPath = ".\\out\\ZTESentenceTypeCount";
        sentenceTypeCount.count(readPath, outPath, LongSentence.UNIT_SHORT_SENTENCE);
    }

    /**
     *
     * @param readPath
     * @param outPath
     * @param punctuationList
     */
    public void count(String readPath, String outPath, String[] punctuationList){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-util.MM-dd-HH-mm-ss");
        Date date = new Date();
        outPath = outPath + String.valueOf(dateFormat.format(date))+".txt";
        ArrayList<String> longSentenceList = TxtOperator.readTxt(readPath);

        String content;

        for (String longSentence : longSentenceList){
            LongSentence ls = new LongSentence(longSentence);
            ls.segToShort(punctuationList);
            for (ShortSentence ss : ls.getShortSentences()){
//                 //for long sentence type count & contain keywords
//                if (longSentence.contains("影")) {
//                util.ShortSentence ss = new util.ShortSentence();
//                ss.setContent(longSentence.split("\t")[0]);
                    int num;
                    //判断最后一个字符是否是标点，是就去掉，不是则保留
                    if (ss.getContent().endsWith(",") | ss.getContent().endsWith("，") | ss.getContent().endsWith("。") | ss.getContent().endsWith("；") | ss.getContent().endsWith(";")) {
                        content = ss.getContent().substring(0, ss.getContent().length() - 1);//去除句末标点，以免只因句末标点不同而影响匹配
                    } else {
                        content = ss.getContent();
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
//            }
        }
        Map<String, Integer> resultMap = new MapSort().sortMapByValue(countSentenceList); //按Value进行排序

        WriteList.writeList(outPath, resultMap);
    }
    }
