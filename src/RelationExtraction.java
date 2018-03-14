import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

public class RelationExtraction {
//    public String lastPrimaryLocation = "";
    ArrayList<String> PrimaryLocation = new ArrayList<String>();
    HashMap<String,String> map = new HashMap<String, String>();
    Pattern P = Pattern.compile("(主干部位#[0-9]+#)|(区域#[0-9]+#)(细节部位#[0-9]+#)(主干部位#[0-9]+#)");

}
