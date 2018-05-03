import java.io.*;
import java.util.ArrayList;

public class TxtOperator {

    public static ArrayList<String> readTxt(String path){
        ArrayList<String> lineList = new ArrayList<>();
        try{
            File file = new File(path);
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = "";
            line = bufferedReader.readLine();
            while (line != null){
                lineList.add(line);
                line = bufferedReader.readLine();
            }
        }catch ( Exception e){
            e.printStackTrace();
        }
        return lineList;
    }

    static void writeTxt(String path, String content) {
        try{
            File file = new File(path);
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(content);
            bufferedWriter.flush();//把缓存区内容压入文件
            bufferedWriter.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
