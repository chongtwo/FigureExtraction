import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShortSentence {

    public String content ;
    public String semanticSentence;
    public HashMap<String, String> matchedDictionary = new HashMap<>();


    public void setContent(String value){
        content = value;
        semanticSentence = content;
    }

    public void match(){ //把MM单独成类的好处是：换match方法时更方便
        //预处理，忽略“同。。。比较：”“复查：”等
        String postContent = content;
        ArrayList<String> ignoreKey = new ArrayList();
        ignoreKey.add("复查");
        ignoreKey.add("比较");
        ignoreKey.add("片示");

        for (String ignoreWord : ignoreKey){
            if (content.contains(ignoreWord)){
                int startIndex = content.indexOf(ignoreWord) + ignoreWord.length()+1;
                postContent = content.substring(startIndex);
            }
        }

//        if(content.contains("复查")|content.contains("比较")|content.contains("片示")){
//            int startIndex = content.indexOf("复查") + 3;
//            postContent = content.substring(startIndex);
//        }
//        if(content.contains("复查：")|content.contains("比较：")|content.contains("现片示：")){
//            String[] conList = content.split("：");
//            int last = conList.length-1;
//            postContent = conList[last];
//        }

        MatchResult matchResult = RMM.maxMatching(postContent, true);
        //匹配阿拉伯数字
        MatchResult figureResult = FigureMatch.figureMatch(matchResult.semanticSentence, true);

        semanticSentence = matchResult.semanticSentence;
        matchedDictionary = matchResult.matchedDictionary;

        for (Map.Entry<String, String> entry: figureResult.matchedDictionary.entrySet()){
            matchedDictionary.put(entry.getKey(),entry.getValue());
        }
    }


    public void combineWord(){
        int numOfCombine = 0;
        ArrayList<Pattern> patterns = new ArrayList<Pattern>();
        String ruleFilePath = ".\\static\\combine_rule.txt";
        ArrayList<String> rules = TxtOperator.readTxt(ruleFilePath);
        //把rules中的每条rulecomplie好,放到另一个ArrayList中
        for (String r : rules) {
            r = r.split("/")[0];
            Pattern p = Pattern.compile(r);
            patterns.add(p);
        }

        for (Pattern p : patterns){
            Matcher m = p.matcher(semanticSentence);
            while(m.find()){  //m.find()是一个迭代器，若一个句子中对同一个pattern匹配到多个，它会自行迭代
                String combine = "";
                String combineSem = "";
                if ((patterns.indexOf(p) == 0)|(patterns.indexOf(p) ==1)|(patterns.indexOf(p) ==2)|(patterns.indexOf(p) ==3)|(patterns.indexOf(p) ==4)|(patterns.indexOf(p) ==8)){
                    combine = combine + matchedDictionary.get(m.group(1)) + matchedDictionary.get(m.group(2));
                    combineSem = "Diagnosis#0"+ String.valueOf(numOfCombine) + "#";
                    semanticSentence = semanticSentence.replace(m.group(1)+m.group(2),combineSem);
                    matchedDictionary.put(combineSem,combine);
                    matchedDictionary.remove(m.group(1));
                    matchedDictionary.remove(m.group(2));
                }
                else if (patterns.indexOf(p) == 5 |patterns.indexOf(p) == 12 ){
                    combine = combine + matchedDictionary.get(m.group(1)) + matchedDictionary.get(m.group(2)) + matchedDictionary.get(m.group(3));
                    combineSem = "Diagnosis#0" + String.valueOf(numOfCombine) + "#";
                    semanticSentence = semanticSentence.replace(m.group(1)+m.group(2)+m.group(3), combineSem);
                    matchedDictionary.put(combineSem, combine);
                    matchedDictionary.remove(m.group(1));
                    matchedDictionary.remove(m.group(2));
                    matchedDictionary.remove(m.group(3));
                }
                else if((patterns.indexOf(p) == 6)|(patterns.indexOf(p) == 7)){
                    combine = combine + matchedDictionary.get(m.group(1)) + matchedDictionary.get(m.group(2));
                    combineSem = "Descriptor#0" + String.valueOf(numOfCombine) + "#";
                    semanticSentence = semanticSentence.replace(m.group(1)+m.group(2),combineSem);
                    matchedDictionary.put(combineSem, combine);
                    matchedDictionary.remove(m.group(1));
                    matchedDictionary.remove(m.group(2));
                }
                else if (patterns.indexOf(p) == 9| patterns.indexOf(p) == 13 ){
                    combine = combine + matchedDictionary.get(m.group(1)) + matchedDictionary.get(m.group(2)) + matchedDictionary.get(m.group(3));
                    combineSem = "SpecificLocation#0" + String.valueOf(numOfCombine)+"#";
                    semanticSentence = semanticSentence.replace(m.group(1)+m.group(2)+m.group(3), combineSem);
                    matchedDictionary.put(combineSem, combine);
                    matchedDictionary.remove(m.group(1));
                    matchedDictionary.remove(m.group(2));
                    matchedDictionary.remove(m.group(3));
                }
                else if (patterns.indexOf(p) == 10 | patterns.indexOf(p)==14){
                    combine = combine + matchedDictionary.get(m.group(1)) + matchedDictionary.get(m.group(2));
                    combineSem = "SpecificLocation#0" + String.valueOf(numOfCombine)+"#";
                    semanticSentence = semanticSentence.replace(m.group(1)+m.group(2), combineSem);
                    matchedDictionary.put(combineSem, combine);
                    matchedDictionary.remove(m.group(1));
                    matchedDictionary.remove(m.group(2));
                }
//                else if (patterns.indexOf(p) == 11){
//                    combine = combine + matchedDictionary.get(m.group(1)) + matchedDictionary.get(m.group(2));
//                    combineSem = "PrimaryLocation#0" + String.valueOf(numOfCombine)+"#";
//                    semanticSentence = semanticSentence.replace(m.group(1)+m.group(2), combineSem);
//                    matchedDictionary.put(combineSem, combine);
//                    matchedDictionary.remove(m.group(1));
//                    matchedDictionary.remove(m.group(2));
//                }
                numOfCombine++;

            }
        }
//        for(Map.Entry<String, String> entry : matchedDictionary.entrySet()){
//            System.out.println(entry.getKey()+":"+ entry.getValue());
//        }
    }

}
