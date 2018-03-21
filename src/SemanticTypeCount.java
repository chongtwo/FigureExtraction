import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SemanticTypeCount {

    public static void main(String[] args){
        Pattern p = Pattern.compile("(长径|直径(?:约)?)(?:为)?(\\d+(?:\\.\\d+)?)(cm|mm)");
        Matcher m = p.matcher("扫描野左肾见一直径约1.6cm类圆形囊性低密度影，");
        if (m.find()){
            for (int i = 0 ; i <= m.groupCount(); i++){
                System.out.println(m.group(i));
            }
        }

    }
}
