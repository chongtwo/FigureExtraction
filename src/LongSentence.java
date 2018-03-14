import java.util.ArrayList;

public class LongSentence {

    ArrayList<ShortSentence> shortSentences = new ArrayList<>();
    private String content;
    int numOfShortSentences;

    public LongSentence(String value){
        content = value;
    }

    public void segToShort(){
        content = content.replace("。","。\n")
                .replace("，", "，\n")
                .replace("；","；\n")
                .replace(",", ",\n")
                .replace(";",";\n");
        String[] shortSentenceList = content.split("\n");
        numOfShortSentences = shortSentenceList.length;

        for (int i = 0 ; i < shortSentenceList.length ; i++){
            shortSentences.add(new ShortSentence());
            shortSentences.get(i).setContent(shortSentenceList[i]);
        }
    }
}
