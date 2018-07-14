package com.example.algorithm.leetcode;

import java.util.*;

/**
 * @author  xiaoguozi
 * @create  2018/7/14 上午10:10
 * @desc    算法思想 - 数据结构相关
 *
 * 《*》算法思想
 *     一、排序
 *      1、快速选择 - 一般用于求解 Kth Element 问题，可以在 O(N) 时间复杂度，O(1) 空间复杂度完成求解工作。
 *                - 与快速排序一样，快速选择一般需要先打乱数组，否则最坏情况下时间复杂度为 O(N2)。
 *      2、堆排序 - 堆排序用于求解 TopK Elements 问题，通过维护一个大小为 K 的堆，堆中的元素就是 TopK Elements。当然它也可以用于求解 Kth Element 问题，堆顶元素就是 Kth Element。
 *               - 快速选择也可以求解 TopK Elements 问题，因为找到 Kth Element 之后，再遍历一次数组，所有小于等于 Kth Element 的元素都是 TopK Elements。
 *               - 可以看到，快速选择和堆排序都可以求解 Kth Element 和 TopK Elements 问题。
 *      3、桶排序 - 出现频率最多的 k 个数
 *               - 设置若干个桶，每个桶存储出现频率相同的数，并且桶的下标代表桶中数出现的频率，即第 i 个桶中存储的数出现的频率为 i。
 *                 把数都放到桶之后，从后向前遍历桶，最先得到的 k 个数就是出现频率最多的的 k 个数。
 *
 **/
public class Leetcode {

    /**
     * @author  xiaoguozi
     * @create  2018/7/14 上午10:25
     * @desc    Kth Element
     *      排序 ：时间复杂度 O(NlogN)，空间复杂度 O(1)
     **/
    public int findKthLargest_paixu(int[] nums, int k) {
        Arrays.sort(nums);
        return nums[nums.length - k];
    }
    /**
     * @author  xiaoguozi
     * @create  2018/7/14 上午10:28
     * @desc    堆排序 ：时间复杂度 O(NlogK)，空间复杂度 O(K)。
     **/
    public int findKthLargest_duipaixu(int[] nums, int k) {
        PriorityQueue<Integer> pq = new PriorityQueue<>(); // 小顶堆
        for (int val : nums) {
            pq.add(val);
            if (pq.size() > k) // 维护堆的大小为 K
                pq.poll();
        }
        return pq.peek();
    }

    /**
     * @author  xiaoguozi
     * @create  2018/7/14 上午10:52
     * @desc    出现频率最多的 k 个数
     **/
    public static List<Integer> topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> frequencyForNum = new HashMap<>();
        for (int num : nums) {
            frequencyForNum.put(num, frequencyForNum.getOrDefault(num, 0) + 1);
        }
        List<Integer>[] buckets = new ArrayList[nums.length + 1];
        for (int key : frequencyForNum.keySet()) {
            int frequency = frequencyForNum.get(key);
            if (buckets[frequency] == null) {
                buckets[frequency] = new ArrayList<>();
            }
            buckets[frequency].add(key);
        }
        List<Integer> topK = new ArrayList<>();
        for (int i = buckets.length - 1; i >= 0 && topK.size() < k; i--) {
            if (buckets[i] != null) {
                topK.addAll(buckets[i]);
            }
        }
        return topK;
    }



    public static void main(String[] args) {
        int[] nums = {1,1,1,2,2,3};
        System.err.println(topKFrequent(nums,1));
    }


}
