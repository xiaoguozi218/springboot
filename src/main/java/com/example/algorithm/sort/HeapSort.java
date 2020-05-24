package com.example.algorithm.sort;

import java.util.Arrays;

/**
 * 1、堆排序
 * - 堆排序是利用堆这种数据结构而设计的一种排序算法，堆排序是一种选择排序，它的最坏，最好，平均时间复杂度均为O(nlogn)，它也是不稳定排序。
 * - 堆是具有以下性质的完全二叉树：
 *   -每个结点的值都大于或等于其左右孩子结点的值，称为大顶堆；
 *   -或者每个结点的值都小于或等于其左右孩子结点的值，称为小顶堆。
 * - 该数组从逻辑上讲就是一个堆结构，我们用简单的公式来描述一下堆的定义就是：
 *   - 大顶堆：arr[i] >= arr[2i+1] && arr[i] >= arr[2i+2]
 *   - 小顶堆：arr[i] <= arr[2i+1] && arr[i] <= arr[2i+2]
 * 2、堆排序基本思想及步骤
 * - 堆排序的基本思想是：将待排序序列构造成一个大顶堆，此时，整个序列的最大值就是堆顶的根节点。将其与末尾元素进行交换，此时末尾就为最大值。
 *   然后将剩余n-1个元素重新构造成一个堆，这样会得到n个元素的次小值。如此反复执行，便能得到一个有序序列了。
 * - 步骤1 构造初始堆。将给定无序序列构造成一个大顶堆（一般升序采用大顶堆，降序采用小顶堆)
 * - 步骤2 将堆顶元素与末尾元素进行交换，使末尾元素最大。然后继续调整堆，再将堆顶元素与末尾元素交换，得到第二大元素。如此反复进行交换、重建、交换。
 * - 再简单总结下堆排序的基本思路：
 *   - a.将无序序列构建成一个堆，根据升序降序需求选择大顶堆或小顶堆;
 *   - b.将堆顶元素与末尾元素交换，将最大元素"沉"到数组末端;
 *   - c.重新调整结构，使其满足堆定义，然后继续交换堆顶元素与当前末尾元素，反复执行 调整+交换 步骤，直到整个序列有序。
 * 3、最后
 * - 堆排序是一种选择排序，整体主要由 构建初始堆+交换堆顶元素和末尾元素并重建堆 两部分组成。
 * - 其中构建初始堆经推导复杂度为O(n)，在交换并重建堆的过程中，需交换n-1次，而重建堆的过程中，根据完全二叉树的性质，[log2(n-1),log2(n-2)...1]逐步递减，近似为nlogn。
 * - 所以堆排序时间复杂度一般认为就是O(nlogn)级
 *
 */
public class HeapSort {

    public static void main(String []args){
        int[] arr = {9,7,8,5,6,1,3,2,4};
        heapSort(arr);
        System.out.println(Arrays.toString(arr));
    }

    public static void heapSort(int[] arr) {
        //创建堆
        for (int i = arr.length - 1; i >= 0; i--) {
            // 从数组的尾部开始，直到第一个元素(角标为0)
            heapify(arr, i, arr.length);
        }
        //调整堆结构+交换堆顶元素与末尾元素
        for (int i = arr.length - 1; i > 0; i--) {
            //将堆顶元素与末尾元素进行交换
            swap(arr, 0, i);
            //重新对堆进行调整
            heapify(arr, 0, i);
        }
    }


    /**
     * 建堆
     *
     * @param arr          看作是完全二叉树
     * @param currentRootNode 当前父节点位置
     * @param size            节点总数
     */
    public static void heapify(int[] arr, int currentRootNode, int size) {

        if (currentRootNode < size) {
            //左子树和右字数的位置
            int left = 2 * currentRootNode + 1;
            int right = 2 * currentRootNode + 2;

            //把当前父节点位置看成是最大的
            int max = currentRootNode;

            if (left < size) {
                //如果比当前根元素要大，记录它的位置
                if (arr[max] < arr[left]) {
                    max = left;
                }
            }
            if (right < size) {
                //如果比当前根元素要大，记录它的位置
                if (arr[max] < arr[right]) {
                    max = right;
                }
            }
            //如果最大的不是根元素位置，那么就交换
            if (max != currentRootNode) {
                swap(arr, max, currentRootNode);
                //继续比较，直到完成一次建堆
                heapify(arr, max, size);
            }
        }
    }

    /**
     * 完成一次建堆，最大值在堆的顶部(根节点)
     */
    public static void maxHeapify(int[] arrays, int size) {
        // 从数组的开头开始，直到最后一个元素(角标为arrays.length-1)
        for (int i = 0; i < size; i++) {
            heapify(arrays, i, size);
        }

    }

    /**
     * 交换元素
     * @param arr
     * @param a
     * @param b
     */
    public static void swap(int[] arr,int a ,int b){
        int temp = arr[a];  //把数组元素arr[a]的值 赋值给 引用变量temp
        arr[a] = arr[b];
        arr[b] = temp;
    }
}
