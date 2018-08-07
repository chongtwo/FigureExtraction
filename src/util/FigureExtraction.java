package util;

import java.text.SimpleDateFormat;
import java.util.*;

public class FigureExtraction {

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        FigureExtraction figureExtraction = new FigureExtraction();
        ArrayList<String> longSentenceList = TxtOperator.readTxt(".\\static2\\sourcefile.txt");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-util.MM-dd-HH-mm-ss");
        String excelPath = "./out/LHMresult"+ String.valueOf(dateFormat.format(new Date())) + ".xlsx";
        //产生100个随机数，并选出句子
//        util.RandomNum rdmn = new util.RandomNum();
//        ArrayList<Integer> rdmnArrL = rdmn.genRanNum(100,longSentenceList.size()-1);
//        ArrayList<String> selectedLongArrL = new ArrayList<>();
//        for (int i : rdmnArrL){
//            selectedLongArrL.add(longSentenceList.get(i));
//        }
        //for whole test
        ArrayList<String> selectedLongArrL = longSentenceList;
        ArrayList<Integer> rdmnArrL = new ArrayList<>(longSentenceList.size());
        for (int i = 0; i < longSentenceList.size();i++){
            rdmnArrL.add(i);
        }
        //for single test
//        ArrayList<String> selectedLongArrL = new ArrayList<>();
//        selectedLongArrL.add("胸廓对称；左肺见弥漫絮状、斑片状密度增高影，边界不清，部分病变致密，内见支气管充气征，邻近处见两个类圆形结节影。左肺上叶尖后段另见条索状密度增高影及点状高密度影，边界清楚。右肺上叶尖段见絮状密度增高影，密度浅淡，边界模糊。右肺上叶支气管局部扩张；气管居中，支气管及左、右主支气管开口通畅；双肺门未见增大；心脏形态未见异常，纵隔见多个淋巴结增大，内见钙化。左侧后胸壁下见弧形液体样低密度影。");
//        ArrayList<Integer> rdmnArrL = new ArrayList<>();
//        rdmnArrL.add(1);

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
		columnName.add("属性");
        columnName.add("可能性");
        columnName.add("性状");
        columnName.add("诊断");
        columnName.add("变化");
        columnName.add("数量");
        columnName.add("测量位置");
        columnName.add("测量值");
        columnName.add("单位");
        xlsOperator.writeXls(excelPath, columnName);

//        deprecated.RelationExtraction re = new deprecated.RelationExtraction();

        int end = longSentenceList.size();
        for (String longS : longSentenceList.subList(0,end)) {
            numOfLong++;
            int numOfShort = 0;
            System.out.println(String.valueOf("长句编号：" + numOfLong));
            LongSentence longSentence = new LongSentence(longS);
            longSentence.segToShort(LongSentence.UNIT_SHORT_SENTENCE);
            HashMap<Integer, StructuredShortSentence> numMap;
            for (ShortSentence ss : longSentence.getShortSentences()) {
                RE re = new RE(); // ！！！位置
                ss.setContent( ss.getContent().split("\t")[0]); //针对有统计数量的输入文本，只取前面的原句，舍弃后面的统计数字
                numOfShort++;
                System.out.println("短句编号：" + numOfShort);
                ss.match();
                ss = re.combineWord(ss);
                numMap = re.relationExtract(ss.getSemanticSentence(), ss.getMatchedDictionary());

                ArrayList<String> columnContent;
                for(Map.Entry<Integer, StructuredShortSentence> entry : numMap.entrySet()){
                    columnContent = new ArrayList<>();
                    columnContent.add(String.valueOf(selectedNumArrL.get(numOfLong-1)));
                    columnContent.add(ss.getContent());
                    columnContent.add(ss.getSemanticSentence());
                    StructuredShortSentence se = entry.getValue();
                    columnContent.add(se.getPrimaryLocation());
                    columnContent.add(se.getSpecificLocation());
                    columnContent.add(se.getRegion());
					columnContent.add(se.getAttribute());
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
            }
        }
        xlsOperator.writeXls(excelPath, allList);
    }
}


