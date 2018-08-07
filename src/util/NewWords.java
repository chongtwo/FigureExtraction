package util;

import java.util.ArrayList;

public class NewWords {
    String newWord = "";
    static ArrayList<String> newWordList = new ArrayList<>();
    int lastUnmatchedIndex = -1;

    /**
     * 判断字符串是否唯一需要记录，若需要则记录
     *
     * @param str
     * @param curIndex
     */
    public void record(String str, int curIndex) {
        if ((!SpecialChar.isNumeric(str)) & (!SpecialChar.isPunc(str))) {
            if ((curIndex == lastUnmatchedIndex + 1) | (lastUnmatchedIndex == -1)) { //如果上次未匹配与此次未匹配的字下标相差1，则这两个字是邻近的字，将它们组成词记录，且暂时不写入，看下一个字是否相邻
                newWord += str;
            }
//            else if (newWord != ""){ //未匹配的字不相邻，则判断未登录词表中是否已经有，若无则写入
            else {
                boolean uniqueFlag = SpecialChar.isUnique(newWord, newWordList);
                if (uniqueFlag) {
                    newWordList.add(newWord);
                }
                newWord = "";
                newWord += str;
            }
            lastUnmatchedIndex = curIndex;
        } else if (newWord != "") { //是数字或标点，且前面有未登录词留存，就把之前积累的未匹配词压入词表
            boolean uniqueFlag = SpecialChar.isUnique(newWord, newWordList);
            if (uniqueFlag) {
                newWordList.add(newWord);
            }
            newWord = "";
        }
    }
}