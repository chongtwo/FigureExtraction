import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SemanticTypeCount{
    String sentence;
    StringBuilder semanticResult;//语义匹配后的句子
    static ArrayList<String> distinctSemanticList = new ArrayList<>();//无重复的语义匹配句子
    private static final int MAXLEN=4; //最大字符数
    private static int len=MAXLEN; //取词长度
    private static int curIndex=0; //当前下标

    public static void main(String[] args){
        ArrayList<String> longSentenceList = TxtOperator.readTxt(".\\static\\CT胸部平扫约4000份-描述.txt");
//        ArrayList<String> longSentenceList = new ArrayList<>();
//        longSentenceList.add("胸廓对称，");
//        longSentenceList.add("纵膈居中，");
        SemanticTypeCount semanticTypeCount = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String outPath = ".\\out\\SemanticTypeCount"+ String.valueOf(dateFormat.format(new Date()))+".txt";
        for (String longSentence : longSentenceList){
           LongSentence ls = new LongSentence(longSentence);
           ls.segToShort();
           for (ShortSentence ss : ls.shortSentences){
               semanticTypeCount = new SemanticTypeCount();
               semanticTypeCount.sentence = ss.content;
               semanticTypeCount.match();
               semanticTypeCount.dedup();
           }
        }
        semanticTypeCount.outPut(outPath);
    }

    public void match(){

        String dictPath = "D:\\1.放射报告结构化\\术语集\\术语集有语义2017.10.23改2018.03.27.txt";
        len = MAXLEN;
        curIndex = 0;
        semanticResult = new StringBuilder();

        while(curIndex<sentence.length()){
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        new FileInputStream(dictPath),"utf8"));
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
                        semanticResult.append(line[1]);
                        curIndex+=len;
                        len=MAXLEN+1;//因为即便匹配成功，也要执行下面的len--，所以还原len的时候要比MAXLEN大1
                    }
                }
                len--; //所有词遍历完没有匹配到，减短匹配长度len，再进入循环
                if (len==0){
                    semanticResult.append(subStr);
                    curIndex++;
                    len=MAXLEN;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void dedup(){
        if (distinctSemanticList.size() == 0){
            distinctSemanticList.add(semanticResult.toString());
        }
        else{
            boolean isUnique = true;
            for (String string: distinctSemanticList){
                if (semanticResult.toString().equals(string)){
                    isUnique = false;
                    break;
                }
            }
            if(isUnique){
                distinctSemanticList.add(semanticResult.toString());
                System.out.println(semanticResult.toString());
            }
        }

    }

    public void outPut(String outPath){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outPath)));
            bw.write(String.valueOf(distinctSemanticList.size())+ "\n");
            for(String string: distinctSemanticList){
                bw.write(string+"\n");
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
