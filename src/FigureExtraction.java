public class FigureExtraction
{

    public static void main(String[] args){
        //FigureExtraction figureExtraction = new FigureExtraction();
        ShortSentence.intializeDictionary("static\\人工词典积累.xls");
        LongSentence longSentence = new LongSentence("未见异常密度影未见异常密度影。");
        longSentence.segToShort();
        for (ShortSentence ss : longSentence.shortSentences){
            ss.match();
            ss.combineWord();
        }
//        System.out.println(longSentence.numOfShortSentences);
//        for (ShortSentence ss : longSentence.shortSentences){
//            System.out.println(ss.content);
        }
    }

