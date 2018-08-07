package util;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class SemanticTypeCount{
    String sentence;
    String semanticResult = "";
    static ArrayList<String> distinctSemanticList = new ArrayList<>();//无重复的语义匹配句子
    static ArrayList<String> distinctSentenceList = new ArrayList<>();//无重复的原句
    static ArrayList<String> unrecordedWordList;//词表中未登录的词

    public static void main(String[] args){

        ArrayList<String> longSentenceList = TxtOperator.readTxt(".\\static\\CT胸部平扫约4000份-描述.txt");
//        ArrayList<String> longSentenceList = new ArrayList<>();
//        longSentenceList.add("系“左肺上叶鳞癌放化疗后3月”复查：");
//        longSentenceList.add("左肺上叶尖后段另见条索状密度增高影及点状高密度影，");
//        longSentenceList.add("与邻近胸膜粘连；");


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-util.MM-dd-HH-mm-ss");
        Date date = new Date();
        String outPath = ".\\out\\util.SemanticTypeCount"+ String.valueOf(dateFormat.format(date))+".txt";
        String unrecordedPath = ".\\out\\unrecordedWord"+ String.valueOf(dateFormat.format(date)) +".txt";
        String distinctSentencePath = ".\\out\\distinctSentence"+ String.valueOf(dateFormat.format(date)) + ".txt";

        SemanticTypeCount semanticTypeCount;
        String[] punctuationList = {"。", "，", "；",",", ";"};

        for (String longSentence : longSentenceList){
            LongSentence ls = new LongSentence(longSentence);
            ls.segToShort(punctuationList);
            for (ShortSentence ss : ls.getShortSentences()){
                semanticTypeCount = new SemanticTypeCount();
                semanticTypeCount.sentence = ss.content;
                MatchResult matchResult = semanticTypeCount.match();
                semanticTypeCount.semanticResult = matchResult.semanticSentence;
                boolean uniqueFlag = SpecialChar.isUnique(semanticTypeCount.semanticResult, distinctSemanticList);
                if(uniqueFlag){
                    distinctSemanticList.add(semanticTypeCount.semanticResult);
                    distinctSentenceList.add(semanticTypeCount.sentence);
                }
            }
        }

        unrecordedWordList = new NewWords().newWordList;
        WriteList.writeList(outPath, distinctSemanticList);
        WriteList.writeList(unrecordedPath,unrecordedWordList);
        WriteList.writeList(distinctSentencePath, distinctSentenceList);
    }

    public MatchResult match(){
        MatchResult matchResult = RMM.maxMatching(sentence);
        matchResult.semanticSentence = FigureMatch.figureMatch(matchResult.semanticSentence);
        return matchResult;
    }

}
