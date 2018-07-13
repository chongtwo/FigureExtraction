import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShortSentence {

    public String content ;
    public String semanticSentence;
    public HashMap<String, String> matchedDictionary = new HashMap<>();

    public String getContent() {
        return content;
    }

    public String getSemanticSentence() {
        return semanticSentence;
    }

    public void setSemanticSentence(String semanticSentence) {
        this.semanticSentence = semanticSentence;
    }

    public HashMap<String, String> getMatchedDictionary() {
        return matchedDictionary;
    }

    public void setMatchedDictionary(HashMap<String, String> matchedDictionary) {
        this.matchedDictionary = matchedDictionary;
    }


    public void setContent(String value){
        content = value;
//        semanticSentence = content;
    }

    public void match(){ //把MM单独成类的好处是：换match方法时更方便
        //预处理，忽略“同。。。比较：”“复查：”等
        String postContent = content;
        ArrayList<String> ignoreKey = new ArrayList();
        ignoreKey.add("复查示");
        ignoreKey.add("复查");
        ignoreKey.add("比较");
        ignoreKey.add("片示");
        ignoreKey.add("对比");

		postContent = content;
        for (String ignoreWord : ignoreKey){
            if (postContent.contains(ignoreWord)){
                int startIndex = content.indexOf(ignoreWord) + ignoreWord.length()+ 1; //实际需处理的词从ignoreKey之后第二个开始算，之后第一个往往是冒号等标点符号
                try{
                    postContent = content.substring(startIndex);
                }catch (StringIndexOutOfBoundsException e){ //防止ignoreKey作为句尾词
                    postContent = "";
                }
            }
        }
        //反向最大匹配
        MatchResult matchResult = RMM.maxMatching(postContent, true);
        //匹配阿拉伯数字
        MatchResult figureResult = FigureMatch.figureMatch(matchResult.semanticSentence, true);

        //for LHM
//        ArrayList<MatchResult> piars = FigureMatch.pairMatch(matchResult.semanticSentence);
        //end for LHM


        semanticSentence = matchResult.semanticSentence;
        matchedDictionary = matchResult.matchedDictionary;

        for (Map.Entry<String, String> entry: figureResult.matchedDictionary.entrySet()){
            matchedDictionary.put(entry.getKey(),entry.getValue());
        }

        //for LHM

    }
}
