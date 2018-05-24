import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SentenceTypeCount {

//    static ArrayList<String> distinctSentenceList = new ArrayList<>();
    static HashMap<String, Integer> countSentenceList = new HashMap<>();

    public void count(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        Date date = new Date();
        String readPath = ".\\static\\CT胸部平扫约4000份-描述.txt";
        ArrayList<String> longSentenceList = TxtOperator.readTxt(readPath);
        String outPath = ".\\out\\SentenceTypeCount"+String.valueOf(dateFormat.format(date))+".txt";
        String content;


        for (String longSentence : longSentenceList){
            LongSentence ls = new LongSentence(longSentence);
            ls.segToShort();
            for (ShortSentence ss : ls.shortSentences){
                int num;
                //判断最后一个字符是否是标点，是就去掉，不是则保留
                if(ss.content.endsWith(",") | ss.content.endsWith("，")| ss.content.endsWith("。")| ss.content.endsWith("；")|ss.content.endsWith(";")){
                    content = ss.content.substring(0, ss.content.length()-1);//去除句末标点，以免只因句末标点不同而影响匹配
                }else {
                    content = ss.content;
                }
                boolean isContain = countSentenceList.containsKey(content);
                try {
                    num = countSentenceList.get(content);
                }catch (Exception e){
                    num = 1;
                }
                if(isContain){
                    num++;
                }
                countSentenceList.put(content, num); //会覆盖已有的key和value
            }
        }
        Map<String, Integer> resultMap = new MapSort().sortMapByValue(countSentenceList); //按Value进行排序

        WriteList.writeList(outPath, resultMap);
    }


    public static void main(String[] args) {
        SentenceTypeCount sentenceTypeCount = new SentenceTypeCount();
        sentenceTypeCount.count();
    }

}
