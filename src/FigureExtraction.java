public class FigureExtraction
{

    public static void main(String[] args){
        //FigureExtraction figureExtraction = new FigureExtraction();
        ShortSentence.intializeDictionary("static\\人工词典积累.xls");
        LongSentence longSentence = new LongSentence("右肺中叶见一长径约0.4cm结节影，");
        longSentence.segToShort();
        for (ShortSentence ss : longSentence.shortSentences){
            ss.match();
        }
//        System.out.println(longSentence.numOfShortSentences);
//        for (ShortSentence ss : longSentence.shortSentences){
//            System.out.println(ss.content);
        }
    }

