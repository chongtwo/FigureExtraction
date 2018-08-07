package util;

import java.util.ArrayList;

public class LongSentence {

    private ArrayList<ShortSentence> shortSentences = new ArrayList<>();
    private String content;
    private int numOfShortSentences;
    static String[] UNIT_REPORT = {};
    static String[] UNIT_LONG_SENTENCE = {"。"};
    static String[] UNIT_SHORT_SENTENCE = {"。", "，", "；",",", ";"};

    public ArrayList<ShortSentence> getShortSentences() {
        return shortSentences;
    }

    public void setShortSentences(ArrayList<ShortSentence> shortSentences) {
        this.shortSentences = shortSentences;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getNumOfShortSentences() {
        return numOfShortSentences;
    }

    public void setNumOfShortSentences(int numOfShortSentences) {
        this.numOfShortSentences = numOfShortSentences;
    }


    public LongSentence(String value){
        content = value;
    }


    public void segToShort(String[] punctuationList){
        for (String punc : punctuationList){
            content = content.replace(punc, punc+"\n");
        }
        String[] shortSentenceList = content.split("\n");//将上述循环中替换的标点分割成列表
        numOfShortSentences = shortSentenceList.length;

        for (int i = 0 ; i < shortSentenceList.length ; i++){
            shortSentences.add(new ShortSentence());
            shortSentences.get(i).setContent(shortSentenceList[i]);
        }
    }
}
