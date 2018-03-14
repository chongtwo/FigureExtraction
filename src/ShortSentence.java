import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShortSentence {

    static String[] combineRule;
    private String content;
    String semanticSentence;
    String primaryLocation = "";
    String descriptor = "";
    String figure = "";
    static HashMap<String, String> semanticDictionary;
    public HashMap<String, String> matchedDictionary = new HashMap<String, String>();

    /**
     * 导入字典，生成map
     * @param path
     */
    static void intializeDictionary(String path){
        XlsOperator xlsOperator = new XlsOperator();
        semanticDictionary = xlsOperator.readXls(path);
        //打印semanticDictionary
//        for(Map.Entry<String, String> entry : semanticDictionary.entrySet()) {
//            System.out.println(entry.getKey() + " : " + entry.getValue());
//        }
    }

    public void setContent(String value){
        content = value;
        semanticSentence = content;
    }

    public void match(){
        int numOfMatched = 0;
        //通过字典匹配中文字
        StringBuffer stringBuffer = new StringBuffer(content);
        for(Map.Entry<String, String> entry : semanticDictionary.entrySet()){
//            if (semanticSentence.contains(entry.getKey())) {
//                semanticSentence = semanticSentence.replace(entry.getKey(), entry.getValue() + "#" + String.valueOf(numOfMatched) + "#");
//                //建立反向字典
//                matchedDictionary.put(entry.getValue() + "#" + String.valueOf(numOfMatched) + "#", entry.getKey());
//                numOfMatched++;
//            }
            Pattern p = Pattern.compile(entry.getKey());
            Matcher m = p.matcher(stringBuffer);

            while (m.find()){
                matchedDictionary.put(entry.getValue()+ "#" + String.valueOf(numOfMatched) + "#", entry.getKey());
                stringBuffer.replace(m.start(), m.end(), entry.getValue()+ "#" + String.valueOf(numOfMatched) + "#");
                numOfMatched++;
                m = p.matcher(stringBuffer);
            }
        }
        semanticSentence = stringBuffer.toString();

//        //打印反向字典
//        for(Map.Entry<String, String> entry : matchedDictionary.entrySet()){
//            System.out.println(entry.getKey() + " : " + entry.getValue());
//        }

        //匹配阿拉伯数字
        Pattern quantifierPattern = Pattern.compile("(长径约|直径约|大小约为|大小约)(\\d+(?:\\.\\d+)?)(cm|mm)");
        Matcher m = quantifierPattern.matcher(semanticSentence);
        if (m.find()){
            for (int i = 1 ; i <= m.groupCount(); i++){
                System.out.println(m.group(i));
            }
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
        for(Map.Entry<String, String> entry : matchedDictionary.entrySet()){
            System.out.println(entry.getKey()+":"+ entry.getValue());
        }
    }
}
