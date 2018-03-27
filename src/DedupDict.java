import java.util.ArrayList;

public class DedupDict {

    public static void main(String[] args){
        String path = "D:\\1.放射报告结构化\\术语集\\术语集有语义2017.10.23改2018.03.27.txt";
        ArrayList<String> wordAndSemantic =  TxtOperator.readTxt(path);
        ArrayList<String> uniqueWord = new ArrayList<>();
        for (String string: wordAndSemantic){
            String[] result = string.split("\t");
            String word = result[0];
            if(uniqueWord.size() == 0 ){
                uniqueWord.add(word);
            }else{
                boolean isUnique = true;
               for (String uw : uniqueWord){
                   if (uw.equals(word)){
                       isUnique = false;
                       System.out.println(word);
                       break;
                   }
               }
               if (isUnique){
                   uniqueWord.add(word);
               }
            }
        }
    }
}
