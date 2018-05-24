import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FigureExtraction {

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        FigureExtraction figureExtraction = new FigureExtraction();
//        ArrayList<String> longSentenceList = TxtOperator.readTxt("D:\\JavaApp\\FigureExtraction\\out\\201-300-9153.txt");
//        ArrayList<String> longSentenceList = TxtOperator.readTxt(".\\out\\前100句中的影.txt");
        ArrayList<String> longSentenceList = new ArrayList<>();longSentenceList.add("左侧肩胛骨骨折。");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String excelPath = "./out/result"+ String.valueOf(dateFormat.format(new Date())) + ".xlsx";
        figureExtraction.go(longSentenceList, excelPath);
        long endTime = System.currentTimeMillis();
        System.out.println("用时:"+ (endTime-startTime) + "ms");
    }


    public void go(ArrayList<String> longSentenceList, String excelPath) {
        int numOfLong = 0;
        ArrayList<ArrayList<String>> allList = new ArrayList<>();
        XlsOperator xlsOperator = new XlsOperator();
//        String excelPath = "./out/distinctresult"+ String.valueOf(dateFormat.format(new Date())) + ".xlsx";
        //写入表头
        ArrayList<String> columnName = new ArrayList<>();
        columnName.add("No");
        columnName.add("原句");
        columnName.add("语义");
        columnName.add("主干部位");
        columnName.add("细节部位");
        columnName.add("区域");
        columnName.add("可能性");
        columnName.add("性状");
        columnName.add("诊断");
        columnName.add("变化");
        columnName.add("数量");
        columnName.add("测量位置");
        columnName.add("测量值");
        columnName.add("单位");
        xlsOperator.writeXls(excelPath, columnName);

        RelationExtraction re = new RelationExtraction();
        int end = longSentenceList.size();
        for (String longS : longSentenceList.subList(0,end)) {
            numOfLong++;
            int numOfShort = 0;
            System.out.println(String.valueOf("长句编号：" + numOfLong));
            LongSentence longSentence = new LongSentence(longS);
            longSentence.segToShort();
            HashMap<Integer, StructuredShortSentence> numMap;
            for (ShortSentence ss : longSentence.shortSentences) {
                ss.content = ss.content.split("\t")[0];
                numOfShort++;
                System.out.println("短句编号：" + numOfShort);
                ss.match();

                ss.semanticSentence = ss.combineWord();
                numMap = re.relationExtract(ss.semanticSentence, ss.matchedDictionary);

                ArrayList<String> columnContent = null;

                for(Map.Entry<Integer, StructuredShortSentence> entry : numMap.entrySet()){
                    columnContent = new ArrayList<>();
                    columnContent.add(String.valueOf(numOfLong));
                    columnContent.add(ss.content);
                    columnContent.add(ss.semanticSentence);
                    StructuredShortSentence se = entry.getValue();
                    columnContent.add(se.primaryLocation);
                    columnContent.add(se.specificLocation);
                    columnContent.add(se.region);
                    columnContent.add(se.possibility);
                    columnContent.add(se.descriptor);
                    columnContent.add(se.diagnosis);
                    columnContent.add(se.change);
                    columnContent.add(se.quantifier);
                    columnContent.add(se.measureLocation);
                    columnContent.add(se.value);
                    columnContent.add(se.unit);

                    allList.add(columnContent);
                }

//                //打印关系抽取后的结果
//                for (Map.Entry<Integer, StructuredShortSentence> entry : numMap.entrySet()) {
//                    Field[] field = entry.getValue().getClass().getDeclaredFields();//获取实体的类的所有属性，返回Field数组
//                    for (int i = 0; i < field.length; i++) {
//                        try {
//                            Object value = field[i].get(entry.getValue());//类的第i个属性去获得某对象的该属性值
//                            System.out.println(field[i].getName() + ":" + value);
//                        } catch (IllegalAccessException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
            }
        }
        xlsOperator.writeXls(excelPath, allList);
    }
}


