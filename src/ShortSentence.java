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
        for(Map.Entry<String, String> entry : semanticDictionary.entrySet()){
            if (semanticSentence.contains(entry.getKey())) {
                semanticSentence = semanticSentence.replace(entry.getKey(), entry.getValue() + String.valueOf(numOfMatched));
                numOfMatched++;
            }
        }
        System.out.println(semanticSentence);
    }
}
