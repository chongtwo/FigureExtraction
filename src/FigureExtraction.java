import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

public class FigureExtraction {

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        FigureExtraction figureExtraction = new FigureExtraction();
        ArrayList<String> longSentenceList = TxtOperator.readTxt("D:\\JavaApp\\FigureExtraction\\out\\YingSentenceTypeCount2018-06-12-16-23-41.txt");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String excelPath = "./out/ying_result"+ String.valueOf(dateFormat.format(new Date())) + ".xlsx";
        //产生100个随机数，并选出句子
//        RandomNum rdmn = new RandomNum();
//        ArrayList<Integer> rdmnArrL = rdmn.genRanNum(100,longSentenceList.size()-1);
//        ArrayList<String> selectedLongArrL = new ArrayList<>();
//        for (int i : rdmnArrL){
//            selectedLongArrL.add(longSentenceList.get(i));
//        }
        //for test single
        ArrayList<String> selectedLongArrL = new ArrayList<>();
        selectedLongArrL.add("部分病变周围见条索影");
        ArrayList<Integer> rdmnArrL = new ArrayList<>();
        rdmnArrL.add(1);

        figureExtraction.go(selectedLongArrL, excelPath, rdmnArrL);
        long endTime = System.currentTimeMillis();
        System.out.println("用时:"+ (endTime-startTime) + "ms");
    }


    public void go(ArrayList<String> longSentenceList, String excelPath, ArrayList<Integer> selectedNumArrL) {
        int numOfLong = 0;
        ArrayList<ArrayList<String>> allList = new ArrayList<>();
        XlsOperator xlsOperator = new XlsOperator();
        //写入表头
        ArrayList<String> columnName = new ArrayList<>();
        columnName.add("No");
        columnName.add("原句");
        columnName.add("语义");
        columnName.add("核心部位");
        columnName.add("修饰部位");
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

        String[] punctuationList = {"。", "，", "；",",", ";"};

//        deprecated.RelationExtraction re = new deprecated.RelationExtraction();
        RE re = new RE();
        int end = longSentenceList.size();
        for (String longS : longSentenceList.subList(0,end)) {
            numOfLong++;
            int numOfShort = 0;
            System.out.println(String.valueOf("长句编号：" + numOfLong));
            LongSentence longSentence = new LongSentence(longS);
            longSentence.segToShort(punctuationList);
            HashMap<Integer, StructuredShortSentence> numMap;
            for (ShortSentence ss : longSentence.getShortSentences()) {
                ss.content = ss.content.split("\t")[0]; //针对有统计数量的输入文本，只取前面的原句，舍弃后面的统计数字
                numOfShort++;
                System.out.println("短句编号：" + numOfShort);
                ss.match();
                ss.combineWord();
                numMap = re.relationExtract(ss.semanticSentence, ss.matchedDictionary);

                ArrayList<String> columnContent;
                for(Map.Entry<Integer, StructuredShortSentence> entry : numMap.entrySet()){
                    columnContent = new ArrayList<>();
                    columnContent.add(String.valueOf(selectedNumArrL.get(numOfLong-1)));
                    columnContent.add(ss.content);
                    columnContent.add(ss.semanticSentence);
                    StructuredShortSentence se = entry.getValue();
                    columnContent.add(se.getPrimaryLocation());
                    columnContent.add(se.getSpecificLocation());
                    columnContent.add(se.getRegion());
                    columnContent.add(se.getPossibility());
                    columnContent.add(se.getDescriptor());
                    columnContent.add(se.getDiagnosis());
                    columnContent.add(se.getChange());
                    columnContent.add(se.getQuantifier());
                    columnContent.add(se.getMeasureLocation());
                    columnContent.add(se.getValue());
                    columnContent.add(se.getUnit());

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


