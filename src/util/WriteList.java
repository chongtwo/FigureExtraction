package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    public static void writeList(String outPath, Map<String, Integer> contentList){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outPath)));
            bw.write(String.valueOf(contentList.size())+ "\n");
            for(Map.Entry<String, Integer> entry: contentList.entrySet()){
                bw.write(entry.getKey()+"\t"+entry.getValue() +"\n");
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
