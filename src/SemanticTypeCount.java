import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SemanticTypeCount{
    String sentence;
//    StringBuilder semanticResult;//语义匹配后的句子
    String semanticResult = "";
    static ArrayList<String> distinctSemanticList = new ArrayList<>();//无重复的语义匹配句子
    static ArrayList<String> distinctSetenceList = new ArrayList<>();//无重复的原句
    static ArrayList<String> unrecordedWordList = new ArrayList<>();//词表中未登录的词
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
        String unrecordedPath = ".\\out\\unrecordedWord"+ String.valueOf(dateFormat.format(new Date())) +".txt";
        String distinctSentencePath = ".\\out\\distinctSentence"+ String.valueOf(dateFormat.format(new Date())) + ".txt";
        semanticTypeCount.outPut(outPath, distinctSemanticList);
        semanticTypeCount.outPut(unrecordedPath,unrecordedWordList);
        semanticTypeCount.outPut(distinctSentencePath, distinctSetenceList);
    }

    public void match(){

        String dictPath = "D:\\1.放射报告结构化\\术语集\\术语集有语义2017.10.23改2018.03.27.txt";
        len = MAXLEN;
        curIndex = 0;
//        semanticResult = new StringBuilder();

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
                    semanticResult += subStr;
                    curIndex++;
                    len=MAXLEN;
                    //将未收录的词写入文件为日后分析
                    if(unrecordedWordList.size()== 0){
                        unrecordedWordList.add(subStr);
                    }else {
                        boolean isUnique = true;
                        for (String word : unrecordedWordList){
                            if (word.equals(subStr)){
                                isUnique = false;
                                break;
                            }
                        }
                        if(isUnique){
                            unrecordedWordList.add(subStr);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //匹配阿拉伯数字
        Pattern quantifierPattern = Pattern.compile("(?<measureLocation>MeasureLocation#[0-9]+#)?(?:约)?(?:为)?(?<value>\\d+(?:\\.\\d+)?(?:×|x|、\\d+(?:\\.\\d+)?)?(?:×|x\\d+(?:\\.\\d+)?)?)(?<unit>Unit#[0-9]+#)");
        Matcher m = quantifierPattern.matcher(semanticResult);
        int numOfFind = 0;
        while (m.find()){
            semanticResult = semanticResult.replace(m.group("value"), "value");
        }


        //匹配阿拉伯数字
//        Pattern quantifierPattern = Pattern.compile("(?<measureLocation>长径|直径|大小)(?:约)?(?:为)?(?:：)?(?<value>\\d+(?:\\.\\d+)?)(?<unit>cm|mm)");
//        Matcher m = quantifierPattern.matcher(semanticResult);
//
//        while (m.find()){
//            semanticResult = semanticResult.replace(m.group("measureLocation"),"measureLocation" );
//            semanticResult = semanticResult.replace(m.group("value"), "value");
//            semanticResult = semanticResult.replace(m.group("unit"), "unit");
//        }
    }
    public void dedup(){
        if (distinctSemanticList.size() == 0){
            distinctSemanticList.add(semanticResult);
            distinctSetenceList.add(this.sentence);
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
                distinctSetenceList.add(this.sentence);
                System.out.println(semanticResult);
            }
        }

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
