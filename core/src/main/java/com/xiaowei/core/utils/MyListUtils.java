package com.xiaowei.core.utils;

import java.util.ArrayList;
import java.util.List;

public class MyListUtils {

    public static List<List<String>> createList(List<String> targe, int size) {
        List<List<String>> listArr = new ArrayList<List<String>>();
        //获取被拆分的数组个数
        int arrSize = targe.size() % size == 0 ? targe.size() / size : targe.size() / size + 1;
        for (int i = 0; i < arrSize; i++) {
            List<String> sub = new ArrayList<String>();
            //把指定索引数据放入到list中
            for (int j = i * size; j <= size * (i + 1) - 1; j++) {
                if (j <= targe.size() - 1) {
                    sub.add(targe.get(j));
                }
            }
            listArr.add(sub);
        }
        return listArr;
    }

}
