

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpecialChar {

    /**
     * 判断某字符串在列表中是否已存在，不存在才存入
     * @param str
     * @param list
     * @return
     */
    public static boolean isUnique(String str, List<String> list){
        if (list.size() == 0){
            return true;
        }
        else{
            for (String string: list){
                if (str.equals(string)){
                    return false;
                }
            }
            System.out.println(str);
        }
        return true;
    }

    /**
     * 判断某字符是否为数字
     * @param str 可能为中文，也可能是-19162431.1254，不使用BigDecimal的话，变成-1.91624311254E7
     * @return
     */
    public static boolean isNumeric(String str) {
        // 该正则表达式可以匹配所有的数字 包括负数
        Pattern pattern = Pattern.compile("-?[0-9]+(\\.[0-9]+)?");
        String bigStr;
        try {
            bigStr = new BigDecimal(str).toString();
        } catch (Exception e) {
            return false;//异常 说明包含非数字。
        }
        Matcher isNum = pattern.matcher(bigStr); // matcher是全匹配
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 判断某字符是否为标点符号
     * @param str
     * @return
     */

    public static boolean isPunc(String str){
        Pattern pattern = Pattern.compile("，|：|“|”|。|、|\\(|\\)|（|）|-|\\.|×|/|；|x| ");
//        Pattern pattern = Pattern.compile(["，：“”。、()（）-.×/；x"]);
        Matcher isPunc = pattern.matcher(str);
        if(!isPunc.matches()){
            return false;
        }
        return true;
    }
}
