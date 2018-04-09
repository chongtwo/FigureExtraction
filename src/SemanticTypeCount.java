import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SemanticTypeCount{
    String sentence;
//    StringBuilder semanticResult;//语义匹配后的句子
    String semanticResult = "";
    static ArrayList<String> distinctSemanticList = new ArrayList<>();//无重复的语义匹配句子
    static ArrayList<String> distinctSentenceList = new ArrayList<>();//无重复的原句
    static ArrayList<String> unrecordedWordList = new ArrayList<>();//词表中未登录的词
    private static final int MAXLEN=4; //最大字符数
    private int len=MAXLEN; //取词长度
    private int curIndex=0; //当前下标
    private int lastUnmatchedIndex=-1;//上次未匹配到的字的下标

    public static void main(String[] args){

        ArrayList<String> longSentenceList = TxtOperator.readTxt(".\\static\\CT胸部平扫约4000份-描述.txt");
//        ArrayList<String> longSentenceList = new ArrayList<>();
//        longSentenceList.add("系“左肺上叶鳞癌放化疗后3月”复查：");
//        longSentenceList.add("左肺上叶尖后段另见条索状密度增高影及点状高密度影，");
//        longSentenceList.add("与邻近胸膜粘连；");
        SemanticTypeCount semanticTypeCount = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        Date date = new Date();
        String outPath = ".\\out\\SemanticTypeCount"+ String.valueOf(dateFormat.format(date))+".txt";
        String unrecordedPath = ".\\out\\unrecordedWord"+ String.valueOf(dateFormat.format(date)) +".txt";
        String distinctSentencePath = ".\\out\\distinctSentence"+ String.valueOf(dateFormat.format(date)) + ".txt";
        for (String longSentence : longSentenceList){
           LongSentence ls = new LongSentence(longSentence);
           ls.segToShort();
           for (ShortSentence ss : ls.shortSentences){
               semanticTypeCount = new SemanticTypeCount();
               semanticTypeCount.sentence = ss.content;
               semanticTypeCount.match();
               semanticTypeCount.isUnique(semanticTypeCount.semanticResult, distinctSemanticList);
           }
        }

        semanticTypeCount.outPut(outPath, distinctSemanticList);
        semanticTypeCount.outPut(unrecordedPath,unrecordedWordList);
        semanticTypeCount.outPut(distinctSentencePath, distinctSentenceList);
    }

    public void match(){

        String unrecordWord = ""; //缓存没有收录的字，等待成词
        String dictPath = ".\\static\\术语集有语义2017.10.23改2018.03.27.txt";
        len = MAXLEN;
        curIndex = 0;
//        semanticResult = new StringBuilder();

        while(curIndex<sentence.length()){
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(dictPath),"utf8"));
                String subStr;//待匹配的字符串，类似滑动的窗口
                if (curIndex+len>sentence.length()){//处理最后几个字
                    subStr=sentence.substring(curIndex,sentence.length());
                }
                else{
                    subStr=sentence.substring(curIndex,curIndex+len);
                }
                String string; //整句话
                while ((string=br.readLine())!=null){ //br.readLine()每次读一个词
                    String[]  line=string.split("\t");
                    if (line[0].equals(subStr)){
//                        semanticResult.append(line[1]);
                        semanticResult += line[1];
                        curIndex+=len;
                        len=MAXLEN+1;//因为即便匹配成功，也要执行下面的len--，所以还原len的时候要比MAXLEN大1
                        break;
                    }
                }
                len--; //所有词遍历完没有匹配到，减短匹配长度len，再进入循环
                if (len==0){ //词典中未收录该词
//                    semanticResult.append(subStr);
                    semanticResult += subStr; //subStr此时一定是一个字，因为len已经从大到小递减过了，已到最小1个字了
                    if((!isNumeric(subStr)) & (!isPunc(subStr)) ) {
                        if ((curIndex == lastUnmatchedIndex + 1)|(lastUnmatchedIndex == -1)) { //如果上次未匹配与此次未匹配的字下标相差1，则这两个字是邻近的字，将它们组成词记录，且暂时不写入，看下一个字是否相邻
                            unrecordWord += subStr;
                        }
                        else if (unrecordWord != ""){ //未匹配的字不相邻，则判断未登录词表中是否已经有，若无则写入
                            if (unrecordedWordList.size() == 0) {
                                unrecordedWordList.add(unrecordWord);
                            }
                            else{
                                boolean isUnique = true;
                                for (String word : unrecordedWordList) {
                                    if (word.equals(unrecordWord)) {
                                        isUnique = false;
                                        break;
                                    }
                                }
                                if (isUnique) {
                                    unrecordedWordList.add(unrecordWord);
                                }
                            }
                            unrecordWord = "";
                            unrecordWord += subStr;
                        }
                        lastUnmatchedIndex = curIndex;
                    }
                    else if(unrecordWord != ""){ //是数字或标点，且前面有未登录词留存，就把之前积累的未匹配词压入词表
                        if (unrecordedWordList.size() == 0) {
                            unrecordedWordList.add(unrecordWord);
                        }
                        else{
                            boolean isUnique = true;
                            for (String word : unrecordedWordList) {
                                if (word.equals(unrecordWord)) {
                                    isUnique = false;
                                    break;
                                }
                            }
                            if (isUnique) {
                                unrecordedWordList.add(unrecordWord);
                            }
                        }
                        unrecordWord = "";
                    }
                    curIndex++;
                    len=MAXLEN;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //匹配阿拉伯数字
        Pattern quantifierPattern = Pattern.compile("(?<measureLocation>MeasureLocation#[0-9]+#)?(?:约)?(?:为)?(?<value>(?:-)?\\d+(?:\\.\\d+)?(?:(x|×|、|~|-)\\d+(?:\\.\\d+)?)?(?:(x|×|、|~)\\d+(?:\\.\\d+)?)?)(?<unit>Unit#[0-9]+#)");
        Matcher m = quantifierPattern.matcher(semanticResult);
        int numOfFind = 0;
        while (m.find()){
            semanticResult = semanticResult.replace(m.group("value"), "value");
        }
    }
    //判断是否唯一
    public void isUnique(String str, List list){
        if (distinctSemanticList.size() == 0){
            distinctSemanticList.add(semanticResult);
            distinctSentenceList.add(this.sentence);
        }
        else{
            boolean isUnique = true;
            for (String string: distinctSemanticList){
                if (semanticResult.equals(string)){
                    isUnique = false;
                    break;
                }
            }
            if(isUnique){
                distinctSemanticList.add(semanticResult);
                distinctSentenceList.add(this.sentence);
                System.out.println(semanticResult);
            }
        }

    }
    /**
     * 匹配是否为数字
     * @param str 可能为中文，也可能是-19162431.1254，不使用BigDecimal的话，变成-1.91624311254E7
     * @return
     * @author yutao
     * @date 2016年11月14日下午7:41:22
     */
    public static boolean isNumeric(String str) {
        // 该正则表达式可以匹配所有的数字 包括负数
        Pattern pattern = Pattern.compile("-?[0-9]+(\\.[0-9]+)?");
        String bigStr;
        try {
            bigStr = new BigDecimal(str).toString();
        } catch (Exception e) {
            return false;//异常 说明包含非数字。
        }

        Matcher isNum = pattern.matcher(bigStr); // matcher是全匹配
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    public static boolean isPunc(String str){
        Pattern pattern = Pattern.compile("，|：|“|”|。|、|\\(|\\)|（|）|-|\\.|×|/|；|x");
        Matcher isPunc = pattern.matcher(str);
        if(!isPunc.matches()){
            return false;
        }
        return true;
    }


    public void outPut(String outPath, ArrayList<String> contentList){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outPath)));
            bw.write(String.valueOf(contentList.size())+ "\n");
            for(String string: contentList){
                bw.write(string+"\n");
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
