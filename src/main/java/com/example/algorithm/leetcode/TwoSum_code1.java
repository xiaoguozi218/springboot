package com.example.algorithm.leetcode;

import java.util.HashMap;
import java.util.Map;

/**
 * 1. 两数之和
 * @author  gsh
 * @date  2020/5/1 下午10:41
 **/
public class TwoSum_code1 {

    /**
     * 思路
     * 标签：哈希映射
     * 这道题本身如果通过暴力遍历的话也是很容易解决的，时间复杂度在 O(n2)
     * 由于哈希查找的时间复杂度为 O(1)，所以可以利用哈希容器 map 降低时间复杂度
     * 遍历数组 nums，i 为当前下标，每个值都判断map中是否存在 target-nums[i] 的 key 值
     * 如果存在则找到了两个值，如果不存在则将当前的 (nums[i],i) 存入 map 中，继续遍历直到找到为止
     * 如果最终都没有结果则抛出异常 或者 返回null
     * 时间复杂度：O(n)
     *
     * @author  gsh
     * @date  2020/5/1 下午10:42
     * @Param
     * @return
     * @throws
     **/
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for(int i = 0; i< nums.length; i++) {
            if(map.containsKey(target - nums[i])) {
                return new int[] {map.get(target-nums[i]),i};
            }
            map.put(nums[i], i);
        }
        return null;
    }

}
