import com.sun.org.apache.xpath.internal.operations.Variable;

import javax.lang.model.element.VariableElement;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RelationExtraction {

    static String lastPrimaryLocation = "";
    static Pattern P = Pattern.compile("(PrimaryLocation#[0-9]+#)|(Region#[0-9]+#)(SpecificLocation#[0-9]+#)(PrimaryLocation#[0-9]+#)");


    public static HashMap<Integer, StructuredShortSentence> relationExtract(String semanticSentence, HashMap<String, String> matchedDictionary){
        String primaryLocation = "";
        String specificLocation = "";
        String region = "";
        String descriptor = "";
        String diagnosis = "";
        String quantifier = "";
        String change = "";
        String possibility = "";
        String measureLocation = "";
        String value= "";
        String unit = "";
        Matcher m = P.matcher(semanticSentence);
        int numOfFind = 0;
        HashMap<Integer, StructuredShortSentence> numMap = new HashMap<>();//用于记录有几个find
        numMap.put(numOfFind, new StructuredShortSentence());//先初始化，避免m.find为空时(句子缺少主干部位时)的空指针，如果m.find不为空，numMap.put将会覆盖该条
        numMap.get(numOfFind).primaryLocation = lastPrimaryLocation;//primaryLocation默认是上次的，如果有新的，则在while(m.find())中更新
        while(m.find()){
            primaryLocation = lastPrimaryLocation;//primaryLocation默认是上次的，如果有新的，则在while(m.find())中更新
            specificLocation = "";
            region = "";
            numMap.put(numOfFind, new StructuredShortSentence());
            for( int index = 1; index <= m.groupCount(); index++){
                if(m.group(index)!= null){
                    if(m.group(index).contains("PrimaryLocation")){
                        primaryLocation = matchedDictionary.get(m.group(index))+ ",";
                    }
                    else if(m.group(index).contains("SpecificLocation")){
                        specificLocation = matchedDictionary.get(m.group(index))+",";
                    }
                    else if (m.group(index).contains("Region")){
                        region = matchedDictionary.get(m.group(index))+",";
                    }
                    matchedDictionary.remove(m.group(index));
                }
            }
            numMap.get(numOfFind).primaryLocation = primaryLocation;
            numMap.get(numOfFind).specificLocation = specificLocation;
            numMap.get(numOfFind).region = region;
            numOfFind++;
            lastPrimaryLocation = primaryLocation;
        }

        specificLocation = "";//清空
        region = "";

        //将匹配剩下的词归类
        for (Map.Entry<String, String> entry : matchedDictionary.entrySet()) {
            if (entry.getKey().contains("SpecificLocation")) {
                specificLocation += entry.getValue()+","; }
            else if(entry.getKey().contains("Region")){
                 region += entry.getValue()+",";
            }
            else if(entry.getKey().contains("Descriptor")){
                descriptor += entry.getValue()+",";
            }
            else if(entry.getKey().contains("Diagnosis")){
                diagnosis += entry.getValue()+",";
            }
            else if(entry.getKey().contains("Quantifier")){
                quantifier += entry.getValue()+",";
            }
            else if(entry.getKey().contains("Change")){
                change += entry.getValue()+",";
            }
            else if (entry.getKey().contains("Possibility")){
                possibility += entry.getValue()+",";
            }
            else if (entry.getKey().contains("MeasureLocation")){
                measureLocation += entry.getValue() + ",";
            }
            else if(entry.getKey().contains("value")){
                value += entry.getValue() + ",";
            }
            else if (entry.getKey().contains("Unit")){
                unit += entry.getValue() + ",";
            }
        }
        //将归类后的词放入对象中
        int j = 0;
        do{
            numMap.get(j).specificLocation += specificLocation; //+=防止前面已有specification存在实例变量里了
            numMap.get(j).region += region;
            numMap.get(j).descriptor += descriptor;
            numMap.get(j).diagnosis += diagnosis;
            numMap.get(j).quantifier += quantifier;
            numMap.get(j).change += change;
            numMap.get(j).possibility += possibility;
            numMap.get(j).measureLocation += measureLocation;
            numMap.get(j).value += value;
            numMap.get(j).unit += unit;
            j++;
        }while( j < numOfFind );
        return numMap;
    }
}
