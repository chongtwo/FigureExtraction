import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class RMM {
    private static StringBuilder result=new StringBuilder(); //存放分词结果，其实也不应该放在属性这里
    private static final int MAXLEN=6; //最大字符数
    private static int len=MAXLEN; //取词长度
    private static int curIndex=0; //当前下标
    private static String dictPath = ".\\static\\term.txt";
    static ArrayList<String> unrecordedWordList = new ArrayList<>();//词表中未登录的词
    static MatchResult matchResult = new MatchResult();


    public static void main(String[] args) {
//        Scanner scanner=new Scanner(System.in);
//        String sentence=scanner.next();
        String sentence = "双肺野清晰，";
        MatchResult matchResult = maxMatching(sentence,true);
        System.out.println(matchResult.semanticSentence);
    }

    public static MatchResult maxMatching(String sentence){
        boolean isNumbered = false;
        MatchResult matchResult = maxMatching(sentence, isNumbered);
        return matchResult;
    }

    public static MatchResult maxMatching(String sentence, boolean numbered){
        MatchResult matchResult;
        if(numbered){
            matchResult = numberedMM(sentence);
        }
        else{
            matchResult = noNumberedMM(sentence);
        }
        return matchResult;
    }

    private static MatchResult noNumberedMM(String sentence) {

        NewWords newWords = new NewWords();
        String semanticResult = "";

        len = MAXLEN;
        curIndex = sentence.length();
        while(curIndex>0){
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        new FileInputStream(dictPath),"utf8"));
                String subStr;//待匹配的字符串，类似滑动的窗口
                if (curIndex-len<0){//处理最后几个字
                    subStr=sentence.substring(0, curIndex);
                }
                else{
                    subStr=sentence.substring(curIndex-len,curIndex);
                }
                String string; //整句话
                while ((string=br.readLine())!=null){ //br.readLine()每次读一个词
                    String[]  line=string.split("\t");
                    if (line[0].equals(subStr)){
                        semanticResult = line[1] + semanticResult;
                        curIndex-=len;
                        len=MAXLEN+1;//因为即便匹配成功，也要执行下面的len--，所以还原len的时候要比MAXLEN大1
                        break;
                    }
                }
                len--; //所有词遍历完没有匹配到，减短匹配长度len，再进入循环
                if (len==0){ //词典中未收录该词
                    semanticResult = subStr + semanticResult; //subStr此时一定是一个字，因为len已经从大到小递减过了，已到最小1个字了
                    newWords.record(subStr, curIndex);
                    curIndex--;
                    len=MAXLEN;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        //匹配阿拉伯数字
//        semanticResult = FigureMatch.figureMatch(semanticResult);

        matchResult.semanticSentence = semanticResult; //matchResult.matchedDictionary = null 没有序号的反向词典是无法建立的
        return matchResult;
    }

//    private static String figureMatch(String semanticResult) {
//        Pattern quantifierPattern = Pattern.compile(
//                "(?<measureLocation>MeasureLocation#[0-9]+#)?(?:约)?(?:为)?(?<value>(?:-)?\\d+(?:\\.\\d+)?(?:(x|×|、|~|-)\\d+(?:\\.\\d+)?)?(?:(x|×|、|~)\\d+(?:\\.\\d+)?)?)(?<unit>Unit#[0-9]+#)");
//        Matcher m = quantifierPattern.matcher(semanticResult);
//        while (m.find()){
//            semanticResult = semanticResult.replace(m.group("value"), "value");
//        }
//        return semanticResult;
//    }

    public static MatchResult numberedMM(String sentence){
        len = MAXLEN;
        curIndex = sentence.length();
        matchResult = new MatchResult();
        String semanticResult = "";
        HashMap<String, String> matchedDictionary = new HashMap<>();
        int numOfMatched=0;
        while(curIndex>0){
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        new FileInputStream(dictPath), "utf8"));
                String subStr;//待匹配的字符串，类似滑动的窗口
                if (curIndex-len<0){//处理最后几个字
                    subStr=sentence.substring(0,curIndex);
                }
                else{
                    subStr=sentence.substring(curIndex-len, curIndex);
                }
                String string; //整句话
                while ((string=br.readLine())!=null){ //br.readLine()每次读一个词
                    String[]  line=string.split("\t");
                    if (line[0].equalsIgnoreCase(subStr)){
                        result.append(subStr).append("/");
                        semanticResult = line[1]+ "#" + String.valueOf(numOfMatched) + "#" + semanticResult;
                        matchedDictionary.put(line[1]+ "#" + String.valueOf(numOfMatched) + "#", subStr);
                        numOfMatched++;
                        curIndex-=len;
                        len=MAXLEN+1;//因为即便匹配成功，也要执行下面的len--，所以还原len的时候要比MAXLEN大1
                    }
                }
                len--; //所有词遍历完没有匹配到，减短匹配长度len，再进入循环
                if (len==0){
                    result.append(subStr).append("/");
                    semanticResult = subStr + semanticResult;
                    curIndex--;
                    len=MAXLEN;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        matchResult.matchedDictionary = matchedDictionary;
        matchResult.semanticSentence = semanticResult.toString();
        return matchResult;
    }
}