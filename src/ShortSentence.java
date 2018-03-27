import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShortSentence {

    public String content ;
    String semanticSentence;
    public HashMap<String, String> matchedDictionary = new HashMap<>();


    public void setContent(String value){
        content = value;
        semanticSentence = content;
    }

    public void match(){ //把MM单独成类的好处是：换match方法时更方便
        MatchResult matchResult = MM.maxMatching(content);
        semanticSentence = matchResult.semanticSentence;
        matchedDictionary = matchResult.matchedDictionary;
//        //打印反向字典
//        for(Map.Entry<String, String> entry : matchedDictionary.entrySet()){
//            System.out.println(entry.getKey() + " : " + entry.getValue());
//        }

        //匹配阿拉伯数字
        Pattern quantifierPattern = Pattern.compile("(?<measureLocation>长径|直径|大小)(?:约)?(?:为)?(?<value>\\d+(?:\\.\\d+)?)(?<unit>cm|mm)");
        Matcher m = quantifierPattern.matcher(semanticSentence);
        int numOfFind = 0;
        while (m.find()){
            matchedDictionary.put("measureLocation#"+String.valueOf(numOfFind)+"#",m.group("measureLocation"));
            matchedDictionary.put("value#"+String.valueOf(numOfFind)+"#",m.group("value"));
            matchedDictionary.put("unit#"+String.valueOf(numOfFind)+"#",m.group("unit"));
            numOfFind++;
//            for (int i = 1 ; i <= m.groupCount(); i++){
//                System.out.println(m.group(i));
//            }
        }
//        System.out.println(content + ":" + semanticSentence);
    }


    public void combineWord(){
        int numOfCombine = 0;
        ArrayList<String> rules;
        ArrayList<Pattern> patterns = new ArrayList<Pattern>();
//        TxtOperator txtOperator = new TxtOperator();
        String ruleFilePath = ".\\static\\combine_rule.txt";
        rules = TxtOperator.readTxt(ruleFilePath);
        //把rules中的每条rulecomplie好,放到另一个ArrayList中
        for (String r : rules) {
            Pattern p = Pattern.compile(r);
            patterns.add(p);
        }

        for (Pattern p : patterns){
            Matcher m = p.matcher(semanticSentence);
            while(m.find()){  //m.find()是一个迭代器，若一个句子中对同一个pattern匹配到多个，它会自行迭代
                     String combine = "";
                     if ((patterns.indexOf(p) == 0)|(patterns.indexOf(p) ==1)|(patterns.indexOf(p) ==2)){
                        combine = combine + matchedDictionary.get(m.group(1)) + matchedDictionary.get(m.group(2));
                        matchedDictionary.put("Diagnosis$"+ String.valueOf(numOfCombine) + "$",combine);
                        matchedDictionary.remove(m.group(1));
                        matchedDictionary.remove(m.group(2));
                        }
                     else if (patterns.indexOf(p) == 3){
                        combine = combine + matchedDictionary.get(m.group(1)) + matchedDictionary.get(m.group(3));
                        matchedDictionary.put("Diagnosis$" + String.valueOf(numOfCombine) + "$", combine);
                        matchedDictionary.remove(m.group(1));
                        matchedDictionary.remove(m.group(3));
                     }
                     else if((patterns.indexOf(p) == 4)|(patterns.indexOf(p) == 5)){
                         combine = combine + matchedDictionary.get(m.group(1)) + matchedDictionary.get(m.group(2));
                         matchedDictionary.put("Diagnosis$" + String.valueOf(numOfCombine) + "$", combine);
                         matchedDictionary.remove(m.group(1));
                         matchedDictionary.remove(m.group(2));
                        }
                     numOfCombine++;

            }
        }
//        for(Map.Entry<String, String> entry : matchedDictionary.entrySet()){
//            System.out.println(entry.getKey()+":"+ entry.getValue());
//        }
    }

}
