import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShortSentence {

    static String rule;
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
        for(Map.Entry<String, String> entry : semanticDictionary.entrySet()){
            if (semanticSentence.contains(entry.getKey())) {
                semanticSentence = semanticSentence.replace(entry.getKey(), entry.getValue() + "#" + String.valueOf(numOfMatched) + "#");
                //建立反向字典
                matchedDictionary.put(entry.getValue() + "#" + String.valueOf(numOfMatched) + "#", entry.getKey());
                numOfMatched++;
            }
        }
        for(Map.Entry<String, String> entry : matchedDictionary.entrySet()){
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
        //匹配阿拉伯数字
        Pattern quantifierPattern = Pattern.compile("(长径约|直径约|大小约为|大小约)(\\d+(?:\\.\\d+)?)(cm|mm)");
        Matcher m = quantifierPattern.matcher(semanticSentence);
        if (m.find()){
            for (int i = 1 ; i <= m.groupCount(); i++){
                System.out.println(m.group(i));
            }
        }
        System.out.println(content + ":" + semanticSentence);
    }
}
