package util;

import java.util.*;

public class MapSort {


    public static void main(String[] args) {

        Map<String, Integer> map = new TreeMap<String, Integer>();

        map.put("KFC", 6);
        map.put("WNBA", 67);
        map.put("NBA", 78);
        map.put("CBA", 7);

//        Map<String, String> resultMap = sortMapByKey(map);    //按Key进行排序
        Map<String, Integer> resultMap = new MapSort().sortMapByValue(map); //按Value进行排序

        for (Map.Entry<String, Integer> entry : resultMap.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }

    /**
     * 使用 Map按value进行排序
     * @param
     * @return
     */
    public Map<String, Integer> sortMapByValue(Map<String, Integer> oriMap) {
        if (oriMap == null || oriMap.isEmpty()) {
            return null;
        }
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        List<Map.Entry<String, Integer>> entryList = new ArrayList<Map.Entry<String, Integer>>(
                oriMap.entrySet());
        Collections.sort(entryList, new MapValueComparator());
        Iterator<Map.Entry<String, Integer>> iter = entryList.iterator();
        Map.Entry<String, Integer> tmpEntry = null;
        while (iter.hasNext()) {
            tmpEntry = iter.next();
            sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
        }
        return sortedMap;
    }

    class MapValueComparator implements Comparator<Map.Entry<String, Integer>> {
        @Override
        public int compare(Map.Entry<String, Integer> me1, Map.Entry<String, Integer> me2) {

            return me2.getValue()- me1.getValue();
        }


    }



}
