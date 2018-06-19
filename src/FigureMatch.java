import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FigureMatch {

    public static String figureMatch(String sentence){
        boolean isNumbered = false;
        MatchResult matchedResult = figureMatch(sentence, isNumbered);
        return matchedResult.semanticSentence;
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
