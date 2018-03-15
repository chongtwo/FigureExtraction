import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Scanner;

public class MM {
    private static StringBuilder result=new StringBuilder(); //其实也不应该放在属性这里
//    public static StringBuilder semanticResult = new StringBuilder();
//    public static HashMap<String, String> matchedDictionary = new HashMap<String, String>();
    private static final int MAXLEN=4; //最大字符数
    private static int len=MAXLEN; //取词长度
    private static int curIndex=0; //当前下标

    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        String sentence=scanner.next();
        MatchResult matchResult = maxMatching(sentence);
        System.out.println(matchResult.semanticSentence);
    }


    public static MatchResult maxMatching(String sentence){
        MatchResult matchResult = new MatchResult();
        StringBuilder semanticResult = new StringBuilder();
        HashMap<String, String> matchedDictionary = new HashMap<String, String>();


        int numOfMatched=0;

        while(curIndex<sentence.length()){

            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        new FileInputStream(".\\static\\分词词典.txt"),"utf8"));
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
                        result.append(subStr).append("/");
                        semanticResult.append(line[1]+ "#" + String.valueOf(numOfMatched) + "#");
                        matchedDictionary.put(line[1]+ "#" + String.valueOf(numOfMatched) + "#", subStr);
                        numOfMatched++;
                        curIndex+=len;
                        len=MAXLEN+1;//因为即便匹配成功，也要执行下面的len--，所以还原len的时候要比MAXLEN大1
                    }
                }
                len--; //所有词遍历完没有匹配到，减短匹配长度len，再进入循环
                if (len==0){
                    result.append(subStr).append("/");
                    semanticResult.append(subStr);
                    curIndex++;
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