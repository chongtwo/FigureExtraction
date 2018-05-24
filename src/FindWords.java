import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FindWords {

    public int isfind(String str, String wantFind){
        int isFound = 0;
        if(str.contains(wantFind)){
            isFound = 1;
        }
        return isFound;
    }

    public void find(String readPath, String outPath, String wantFind){
        ArrayList<String> SentencesArrayList = TxtOperator.readTxt(readPath);
        ArrayList<String> findResultAryList = new ArrayList<>();
        for (String entry : SentencesArrayList){
            String sentences = entry.split("\t")[0];
            int isFound = isfind(sentences, wantFind);
            String newEntry = entry + "\t" + String.valueOf(isFound);
            findResultAryList.add(newEntry);
        }
        WriteList.writeList(outPath, findResultAryList);
    }

    public static void main(String[] args) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        Date date = new Date();
        String readPath = ".\\out\\SentenceTypeCount2018-05-07-13-39-29.txt";
        String outPath = ".\\out\\FindWords" + simpleDateFormat.format(date) +".txt";
        FindWords findWords = new FindWords();
        String wantFind = "影";
        findWords.find(readPath, outPath, wantFind);
    }

}
