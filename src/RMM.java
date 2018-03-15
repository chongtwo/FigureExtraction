import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;

public class RMM {
    private static LinkedList<String> list=new LinkedList<>();
    private static final int MAXLEN=4; //最大字符数
    private static int len=MAXLEN; //取词长度
    private static int curIndex; //当前下标

    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        String sentence=scanner.next();
        curIndex=sentence.length();
        maxMatching(sentence);
        for (String s:list) {
            System.out.print(s);
        }
    }

    private static void maxMatching(String sentence){
        while(curIndex>0){
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        new FileInputStream(".\\static\\分词词典.txt"),"utf8"));
                String subStr;
                if (curIndex-len<0){
                    subStr=sentence.substring(0,curIndex);
                }
                else {
                    subStr=sentence.substring(curIndex-len,curIndex);
                }
                String string;
                while ((string=br.readLine())!=null){
                    String[] line=string.split(",");
                    if (line[0].equals(subStr)){
                        list.push(subStr+"/");
                        curIndex-=len;
                        len=MAXLEN+1; //因为后面还有个len--一定会执行，所以必须+1
                    }
                }
                len--;
                if (len==0){
                    list.push("该字符不存在/");
                    curIndex--;
                    len=MAXLEN;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
