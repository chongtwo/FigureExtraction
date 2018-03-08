public class FigureExtraction
{

    public static void main(String[] args){
        LongSentence longSentence = new LongSentence("胸廓对称。左上叶有小结节，右肺结节；");
        longSentence.segToShort();
        System.out.println(longSentence.numOfShortSentences);
        for (ShortSentence ss : longSentence.shortSentences){
            System.out.println(ss.content);
        }
    }
}
