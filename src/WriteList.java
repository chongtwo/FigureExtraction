
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class WriteList {

    public static void writeList(String outPath, ArrayList<String> contentList){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outPath)));
            bw.write(String.valueOf(contentList.size())+ "\n");
            for(String string: contentList){
                bw.write(string+"\n");
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
