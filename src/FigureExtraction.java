import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FigureExtraction
{

    public static void main(String[] args){
        FigureExtraction figureExtraction = new FigureExtraction();
        ArrayList<String> longSentenceList = TxtOperator.readTxt(".\\static\\CT胸部平扫约4000份-描述.txt");
//        ArrayList<String> longSentenceList = new ArrayList<>();longSentenceList.add("左肺下叶近胸膜处见一钙化灶影");
        figureExtraction.go(longSentenceList);
    }


    public void go(ArrayList<String> longSentencList){
        int numOfLong = 0;
        for(String longS : longSentencList.subList(0,100)){
            numOfLong++;
            int numOfShort = 0;
            System.out.println(String.valueOf(numOfLong));
            LongSentence longSentence = new LongSentence(longS);
            longSentence.segToShort();
            HashMap<Integer, StructruedEntry> numMap;
            for (ShortSentence ss : longSentence.shortSentences) {
                numOfShort++;
                System.out.println(numOfShort);
                ss.match();
                ss.combineWord();
                numMap = RelationExtraction.relationExtract(ss.semanticSentence, ss.matchedDictionary);

                for (Map.Entry<Integer, StructruedEntry> entry : numMap.entrySet()) {
                    Field[] field = entry.getValue().getClass().getDeclaredFields();//获取实体的类的所有属性，返回Field数组
                    for (int i = 0; i < field.length; i++) {
                        try {
                            Object value = field[i].get(entry.getValue());//类的第i个属性去获得某对象的该属性值
                            System.out.println(field[i].getName() + ":" + value);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}

