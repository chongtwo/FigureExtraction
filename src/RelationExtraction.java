import javax.lang.model.element.VariableElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RelationExtraction {
//    public String lastPrimaryLocation = "";
    ArrayList<String> PrimaryLocation = new ArrayList<String>();
    HashMap<String,String> map = new HashMap<String, String>();
    static Pattern P = Pattern.compile("(PrimaryLocation#[0-9]+#)|(Region#[0-9]+#)(SpecificLocation#[0-9]+#)(PrimaryLocation#[0-9]+#)");


    public static void relationExtraction(String semanticSentence, HashMap<String, String> matchedDictionary){
        String primaryLocation = "";
        String specificLocation = "";
        String region = "";
        String descriptor = "";
        String diagnosis = "";
        String quantifier = "";
        String change = "";
        String possibility = "";
        Matcher m = P.matcher(semanticSentence);
        int numOfFind = 0;
        HashMap<Integer, HashMap> numMap = new HashMap<Integer, HashMap>();//用于记录有几个find

        while(m.find()){
            numMap.put(numOfFind, new HashMap());
            for( int index = 1; index <= m.groupCount(); index++){
                if(m.group(index).contains("PrimaryLocation")){
                    primaryLocation += m.group(index)+ ",";
                }
                else if(m.group(index).contains("SpecificLocation")){
                    specificLocation += m.group(index)+",";
                }
                else if (m.group(index).contains("Region")){
                    region += m.group(index)+",";
                }
                matchedDictionary.remove(m.group(index));
            }
            numMap.get(numOfFind).put("PrimaryLocation", primaryLocation);
            numMap.get(numOfFind).put("SpecificLocation", specificLocation);
            numMap.get(numOfFind).put("Region", region);
            numOfFind++;
        }
        specificLocation = "";//清空
        region = "";
//传入string,用一个map实现string与变量名之间的关系

        for(int i = 0 ; i < numOfFind; i++) {
            for (Map.Entry<String, String> entry : matchedDictionary.entrySet()) {
                if (entry.getKey().contains("SpecificLocation")) {
                    specificLocation += entry.getValue();
                }
                else if(entry.getKey().contains("Region")){
                    region += entry.getValue();
                }
                else if(entry.getKey().contains("Descriptor")){
                    descriptor += entry.getValue();
                }
                else if(entry.getKey().contains("Diagnosis")){
                    diagnosis += entry.getValue();
                }
                else if(entry.getKey().contains("Quantifier")){
                    quantifier += entry.getValue();
                }
                else if(entry.getKey().contains("Change")){
                    change += entry.getValue();
                }
                else if (entry.getKey().contains("Possibility")){
                    possibility += entry.getValue();
                }
            }
            if (numMap.get(i).containsKey("SpecificLocation")){
                //如果原Map已有该key，就取出该key中的value，加上新的value，再放回该key中
                numMap.get(i).put("SpecificLocation", numMap.get(i).get("SpecificLocation")+ specificLocation);
            }
            else {
                numMap.get(i).put("SpecificaLocation", numMap.get(i).put("SpecificLocation", specificLocation));
            }
            //待补充


        }
    }

}
