import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RE {

    private HashMap<Integer, Pattern> bodyPartPatMap;
    private HashMap<Integer, Pattern> exactPatMap;
    boolean isFound = false;


    private void compilePattern() {
        bodyPartPatMap = new HashMap<>();
        exactPatMap = new HashMap<>();
        ArrayList<String> bodyPartStrList = TxtOperator.readTxt(".//static//bodyPart.txt");
        ArrayList<String> exactStrList = TxtOperator.readTxt(".//static//exact.txt");

        for (String str : bodyPartStrList) {
            Integer patternID = Integer.valueOf(str.split(" ")[0]);
            Pattern patternContent = Pattern.compile(str.split(" ")[1]);
            bodyPartPatMap.put(patternID, patternContent);
        }

        for (String str : exactStrList) {
            Integer patternID = Integer.valueOf(str.split(" ")[0]);
            Pattern patternContent = Pattern.compile(str.split(" ")[1]);
            exactPatMap.put(patternID, patternContent);
        }
    }

    public HashMap<Integer,StructuredShortSentence> relationExtract(String semanticSentence, HashMap<String, String> matchedDictionary) {
        compilePattern();
        //先精准匹配
        HashMap<Integer, StructuredShortSentence> numMap = exactRE(semanticSentence, matchedDictionary);
        //精准匹配没有匹配到，再进行部位匹配
        if (!isFound){
            numMap = generalRE(semanticSentence, matchedDictionary);
        }
        return numMap;
    }

    public HashMap<Integer,StructuredShortSentence> exactRE(String semanticSentence, HashMap<String, String> matchedDictionary){

        HashMap<Integer,StructuredShortSentence> numMap = new HashMap<>();
//        numMap.put(0, new StructuredShortSentence());//先初始化，避免m.find为空时(句子缺少主干部位时)的空指针，如果m.find不为空，numMap.put将会覆盖该条

        for (Map.Entry<Integer, Pattern> entry : exactPatMap.entrySet()) {
            Matcher m = entry.getValue().matcher(semanticSentence);
//            Pattern p = Pattern.compile("(<PL1>PrimaryLocation#[0-9]+#)(<SL1>SpecificLocation#[0-9]+#)(<P1>Possibility#[0-9]+#)(<DP1>Descriptor#[0-9]+#)(<DG1>Diagnosis#[0-9]+#)(<CC1>coorConj#[0-9]+#)(<DP2>Descriptor#[0-9]+#)(<DG2>Diagnosis#[0-9]+#)");
            Integer patternID = entry.getKey();
            while (m.find()) {
                isFound = true;
                if (patternID ==  1) {
                    numMap.put(0, new StructuredShortSentence());
                    numMap.put(1, new StructuredShortSentence());
                    numMap.get(0).setDescriptor(matchedDictionary.get(m.group("DP1")));
                    numMap.get(0).setDiagnosis(matchedDictionary.get(m.group("DG1")));
                    numMap.get(1).setDescriptor(matchedDictionary.get(m.group("DP2")));
                    numMap.get(1).setDiagnosis(matchedDictionary.get(m.group("DG2")));
                    for (int i = 0; i <= 1; i++) {
                        numMap.get(i).setPrimaryLocation(matchedDictionary.get(m.group("PL1")));
                        numMap.get(i).setSpecificLocation(matchedDictionary.get(m.group("SL1")));
                        numMap.get(i).setPossibility(matchedDictionary.get(m.group("P1")));
                    }
                }
                else if (patternID == 2) {
                    numMap.put(0, new StructuredShortSentence());
                    numMap.put(1, new StructuredShortSentence());
                    numMap.get(0).setDescriptor(matchedDictionary.get(m.group("DP1"))+ "," + matchedDictionary.get(m.group("DP2")) + "," + matchedDictionary.get(m.group("DP3")));
                    numMap.get(0).setDiagnosis(matchedDictionary.get(m.group("DG1")));
                    numMap.get(1).setDescriptor(matchedDictionary.get(m.group("DP4")));
                    numMap.get(1).setDiagnosis(matchedDictionary.get(m.group("DG2")));
                    numMap.get(1).setQuantifier(matchedDictionary.get(m.group("Q1")));
                    for (int i = 0; i <= 1; i++) {
                        numMap.get(i).setPrimaryLocation(matchedDictionary.get(m.group("PL1")));
                        numMap.get(i).setSpecificLocation(matchedDictionary.get(m.group("SL1")));
                        numMap.get(i).setPossibility(matchedDictionary.get(m.group("P1")));
                    }
                }
                else if (patternID == 3) {
                    numMap.put(0, new StructuredShortSentence());
                    numMap.get(0).setPrimaryLocation(matchedDictionary.get(m.group("PL2")));
                    numMap.get(0).setSpecificLocation(matchedDictionary.get(m.group("PL1"))+matchedDictionary.get(m.group("SL1")));
                    numMap.get(0).setRegion(matchedDictionary.get(m.group("R1")));
                    numMap.get(0).setChange(matchedDictionary.get(m.group("C1")));
                }
                else if (patternID == 4) {
                    numMap.put(0, new StructuredShortSentence());
                    numMap.put(1, new StructuredShortSentence());
                    numMap.get(0).setPrimaryLocation(matchedDictionary.get(m.group("PL1")));
                    numMap.get(1).setPrimaryLocation(matchedDictionary.get(m.group("PL2")));
                    numMap.get(1).setRegion(matchedDictionary.get(m.group("R1"))+ "," + matchedDictionary.get(m.group("R2")));
                    for (int i = 0 ; i <=1 ; i++ ) {
                        numMap.get(i).setSpecificLocation(matchedDictionary.get(m.group("SL1")));
                        numMap.get(i).setDescriptor(matchedDictionary.get(m.group("DP1")));
                    }
                }
                else if (patternID ==  5) { //部分病变周围见条索影
                    numMap.put(0, new StructuredShortSentence());
                    numMap.get(0).setPrimaryLocation(matchedDictionary.get(m.group("DG1")));
                    numMap.get(0).setSpecificLocation(matchedDictionary.get(m.group("R1")));
                    numMap.get(0).setPossibility(matchedDictionary.get(m.group("P1")));
                    numMap.get(0).setDiagnosis(matchedDictionary.get(m.group("DG2")));
                }
                break;
            }
        }
        return numMap;
    }

    public HashMap<Integer,StructuredShortSentence> generalRE(String semanticSentence, HashMap<String, String> matchedDictionary){
        String primaryLocation = "";
        String specificLocation = "";
        String region = "";
        String descriptor = "";
        String diagnosis = "";
        String quantifier = "";
        String change = "";
        String possibility = "";
        String measureLocation = "";
        String value = "";
        String unit = "";
        int numOfFind = 0;

        HashMap<Integer,StructuredShortSentence> numMap = new HashMap<>();
        numMap.put(numOfFind,new StructuredShortSentence());//先初始化，避免m.find为空时(句子缺少主干部位时)的空指针，如果m.find不为空，numMap.put将会覆盖该条

        for (Map.Entry<Integer, Pattern> entry : bodyPartPatMap.entrySet()) {
            Matcher m = entry.getValue().matcher(semanticSentence);
            while (m.find()) {
                specificLocation = "";
                region = "";
                numMap.put(numOfFind, new StructuredShortSentence());
                for (int index = 1; index <= m.groupCount(); index++) {
                    if (m.group(index) != null) {
                        if (m.group(index).contains("PrimaryLocation")) {
                            primaryLocation = matchedDictionary.get(m.group(index)) + ",";
                        } else if (m.group(index).contains("SpecificLocation")) {
                            specificLocation += matchedDictionary.get(m.group(index)) + ",";
                        } else if (m.group(index).contains("Region")) {
                            region += matchedDictionary.get(m.group(index)) + ",";
                        }
                        matchedDictionary.remove(m.group(index));
                    }
                }
                numMap.get(numOfFind).setPrimaryLocation(primaryLocation);
                numMap.get(numOfFind).setSpecificLocation(specificLocation);
                numMap.get(numOfFind).setRegion(region);
                numOfFind++;
                semanticSentence = m.replaceAll("#");
                m = entry.getValue().matcher(semanticSentence);
            }

            specificLocation = "";//清空
            region = "";
        }
        //将匹配剩下的词（公用的词）归类
        for (Map.Entry<String, String> dictEntry : matchedDictionary.entrySet()) {
            if (dictEntry.getKey().contains("SpecificLocation")) {
                specificLocation += dictEntry.getValue() + ",";
            } else if (dictEntry.getKey().contains("Region")) {
                region += dictEntry.getValue() + ",";
            } else if (dictEntry.getKey().contains("Descriptor")) {
                descriptor += dictEntry.getValue() + ",";
            } else if (dictEntry.getKey().contains("Diagnosis")) {
                diagnosis += dictEntry.getValue() + ",";
            } else if (dictEntry.getKey().contains("Quantifier")) {
                quantifier += dictEntry.getValue() + ",";
            } else if (dictEntry.getKey().contains("Change")) {
                change += dictEntry.getValue() + ",";
            } else if (dictEntry.getKey().contains("Possibility")) {
                possibility += dictEntry.getValue() + ",";
            } else if (dictEntry.getKey().contains("MeasureLocation")) {
                measureLocation += dictEntry.getValue() + ",";
            } else if (dictEntry.getKey().contains("value")) {
                value += dictEntry.getValue() + ",";
            } else if (dictEntry.getKey().contains("Unit")) {
                unit += dictEntry.getValue() + ",";
            }
        }
        //将公用的词放入各个对象中
        int j = 0;
        do {
            numMap.get(j).setSpecificLocation(numMap.get(j).getSpecificLocation()+ specificLocation); //+=防止前面已有specification存在实例变量里了
            numMap.get(j).setRegion(numMap.get(j).getRegion()+ region);
            numMap.get(j).setDescriptor(numMap.get(j).getDescriptor()+ descriptor);
            numMap.get(j).setDiagnosis(numMap.get(j).getDiagnosis()+ diagnosis);
            numMap.get(j).setQuantifier(numMap.get(j).getQuantifier()+quantifier);
            numMap.get(j).setChange(numMap.get(j).getChange()+ change);
            numMap.get(j).setPossibility(numMap.get(j).getPossibility()+possibility);
            numMap.get(j).setMeasureLocation(numMap.get(j).getMeasureLocation()+measureLocation);
            numMap.get(j).setValue(numMap.get(j).getValue()+value);
            numMap.get(j).setUnit(numMap.get(j).getUnit()+unit);
            j++;
        } while (j < numOfFind);
        return numMap;
    }
}