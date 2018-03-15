import java.util.HashMap;

public class FigureExtraction
{

    public static void main(String[] args){
        //FigureExtraction figureExtraction = new FigureExtraction();
        ShortSentence.intializeDictionary(".\\static\\人工词典积累.xls");
        LongSentence longSentence = new LongSentence("气管、左右主支气管及其分支开口未见狭窄、中断。");
        longSentence.segToShort();
        HashMap<Integer, StructruedEntry> numMap = new HashMap<Integer, StructruedEntry>();
        for (ShortSentence ss : longSentence.shortSentences){
            ss.match();
            ss.combineWord();
            numMap = RelationExtraction.relationExtract(ss.semanticSentence, ss.matchedDictionary);
        }

//        System.out.println(longSentence.numOfShortSentences);
//        for (ShortSentence ss : longSentence.shortSentences){
//            System.out.println(ss.content);
        }
    }

