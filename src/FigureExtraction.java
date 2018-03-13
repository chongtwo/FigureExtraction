public class FigureExtraction
{

    public static void main(String[] args){
        //FigureExtraction figureExtraction = new FigureExtraction();
        ShortSentence.intializeDictionary("C:\\Users\\W\\Desktop\\人工词典积累.xls");
        LongSentence longSentence = new LongSentence("胸廓对称。左上叶有小结节，右肺结节；");
        longSentence.segToShort();
//        System.out.println(longSentence.numOfShortSentences);
//        for (ShortSentence ss : longSentence.shortSentences){
//            System.out.println(ss.content);
        }
    }

