package util;

import java.util.ArrayList;
import java.util.Random;

public class RandomNum {

    public static void main(String[] args) {
        RandomNum randomNum = new RandomNum();
        randomNum.genRanNum(10,3133);
    }

    /**
     *
     * @param totalNum 需产生的随机数个数
     * @param range 产生随机数的范围[1,range]，range可以取到
     * @return
     */

    public ArrayList<Integer> genRanNum(int totalNum, int range){
        ArrayList<Integer> resultNum = new ArrayList<>();
        int count = 0;
        while (count < totalNum){
            Random rdm = new Random();
            int rdmNum = rdm.nextInt(range)+1;//产生[1,range]的整数
            if (resultNum.indexOf(rdmNum) == -1){
                resultNum.add(rdmNum);
                count++;
            }
        }
        for(int i : resultNum){
            System.out.println(i);
        }
        return resultNum;
    }
}
