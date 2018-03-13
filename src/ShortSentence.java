import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class ShortSentence {

    static HashMap<String, String> semanticDictionary;

    /**
     * 导入字典，生成map
     * @param path
     */
    static void intializeDictionary(String path){
        XlsOperator xlsOperator = new XlsOperator();
        semanticDictionary = xlsOperator.readXls(path);
        for(Map.Entry<String, String> entry : semanticDictionary.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }

    static String rule;

    String content;
    String primaryLocation;
    String figure;


    public void setContent(String value){
        content = value;
    }

    public void match(){

    }
}
