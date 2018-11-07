package com.example.java8;

import java.util.HashMap;
import java.util.Map;

/**
 * 前面提到过，Map类型不支持stream，不过Map提供了一些新的有用的方法来处理一些日常任务。
 *
 */
public class MapTest {
    public static void main(String[] args) {
        Map<Integer, String> map = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            map.putIfAbsent(i, "val" + i);
        }
//        map.forEach((id, val) -> System.out.println(id+","+val));

        map.computeIfPresent(3,(num,val) -> val+num);
        System.out.println(map.get(3));

    }
}
