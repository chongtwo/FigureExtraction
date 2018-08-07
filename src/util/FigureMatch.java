package util;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FigureMatch {

    public static String figureMatch(String sentence){
        boolean isNumbered = false;
        MatchResult matchedResult = figureMatch(sentence, isNumbered);
        return matchedResult.semanticSentence;
    }

    /**
     * for LHM
     * @param
     * @return
     */
    public static ArrayList<MatchResult> pairMatch(MatchResult mr){

        ArrayList<MatchResult> matchResultArrayList = new ArrayList<>();

        Pattern SVPattern = Pattern.compile("SV置于(?<SVP>.*?)见.*?(?<measureLocation>MeasureLocation#[0-9]+#)");
        Matcher msv = SVPattern.matcher(mr.semanticSentence);
        while (msv.find()){
            System.out.println("find");
            MatchResult matchResult = new MatchResult();
            if(msv.group("SVP") != null){
                matchResult.matchedDictionary.put("SVplace",msv.group("SVP"));
            }
            matchResultArrayList.add(matchResult);
        }


        Pattern quantifierPattern = Pattern.compile("(?<measureLocation>MeasureLocation#[0-9]+#)(?::)?(?:Φ)?(\\s*)(?<value>(?:-)?\\d+(?:\\.\\d+)?(?:(\\+|x|×|、|~|-|—)\\d+(?:\\.\\d+)?)?(?:(x|×|、|~)\\d+(?:\\.\\d+)?)?)(?<unit>Unit#[0-9]+#)?");
        Matcher m = quantifierPattern.matcher(mr.semanticSentence);
        int numOfFind = 0;
        while(m.find()){
            MatchResult matchResult = new MatchResult();
            matchResult.matchedDictionary.put("value#"+String.valueOf(numOfFind)+"#",m.group("value"));
            matchResult.matchedDictionary.put(m.group("measureLocation"), mr.matchedDictionary.get(m.group("measureLocation")));
            mr.semanticSentence = mr.semanticSentence.replace(m.group("value"),"value#"+String.valueOf(numOfFind)+"#");
            numOfFind++;
            matchResultArrayList.add(matchResult);
        }
        return matchResultArrayList;
    }


    public static MatchResult figureMatch(String sentence, boolean isNumbered){
        Pattern quantifierPattern = Pattern.compile("(?<measureLocation>MeasureLocation#[0-9]+#)?(?:约)?(?:为)?(?<value>(?:-)?\\d+(?:\\.\\d+)?(?:(x|×|、|~|-|—)\\d+(?:\\.\\d+)?)?(?:(x|×|、|~)\\d+(?:\\.\\d+)?)?)(?<unit>Unit#[0-9]+#)");
        Matcher m = quantifierPattern.matcher(sentence);
        MatchResult matchResult = new MatchResult();

        if(isNumbered){
            int numOfFind = 0;
            while (m.find()){
                matchResult.matchedDictionary.put("value#"+String.valueOf(numOfFind)+"#",m.group("value"));
                sentence = sentence.replace(m.group("value"),"value#"+String.valueOf(numOfFind)+"#");
                numOfFind++;
            }
            matchResult.semanticSentence = sentence;
        }
        else{
            while (m.find()){
                sentence = sentence.replace(m.group("value"), "value");
            }
            matchResult.semanticSentence = sentence;
        }
        return matchResult;
    }
}
