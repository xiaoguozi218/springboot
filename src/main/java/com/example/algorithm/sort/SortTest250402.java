package com.example.algorithm.sort;

import java.util.Arrays;

public class SortTest250402 {


    public static void main(String[] args) {
        int[] arr = {6,7,8,5,9,1,2,3,4};
//        int[] arr = {1,2,3,4};
//        selectionSort2(arr);
//        selectionSort(arr);
//        bubbleSort(arr);
//        bubbleSort2(arr);
//        quickSort3(arr, 0, arr.length-1);
//        insertSort(arr);
        mergeSort(arr, 0, arr.length-1);
        System.err.println(Arrays.toString(arr));
    }

    private static void swap(int[] arr, int a, int b) {
        int temp = arr[a];
        arr[a] = arr[b];
        arr[b] = temp;
    }

    public static void selectionSort(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = i+1; j < arr.length; j++) {
                if (arr[j] < arr[i]) {
                    swap(arr, i, j);
                }
            }
        }
    }

    public static void selectionSort2(int[] arr){
        for(int i = 0; i < arr.length; i++){
            int min = i;
            for(int j = i+1; j < arr.length; j++){
                if(arr[j] < arr[min]){
                    min = j;
                }
            }
            swap(arr, i, min);
        }
    }

    public static void bubbleSort(int[] arr) {
        // 统计执行次数
        int count = 0;
        // 外层循环控制排序的轮数，每轮找出一个最大值
        for (int i = arr.length-1; i > 0 ; i--) {
            // 内层循环用于比较相邻的两个元素，并在必要时交换它们
            for (int j = 0; j < i; j++) {
                // 如果当前元素大于下一个元素，则交换它们的位置
                if(arr[j]>arr[j+1]){
                    swap(arr,j,j+1);
                }
                count++;
            }
        }
        System.err.println("bubbleSort执行次数："+count);
    }

    /**
     * 使用冒泡排序算法对数组进行排序
     * 该方法实现的是冒泡排序的优化版本，当一轮比较中没有发生交换时，说明数组已经有序，可以提前结束排序
     *
     * @param arr 待排序的整型数组
     */
    public static void bubbleSort2(int[] arr) {
        boolean isSwap = false;
        //统计执行次数
        int count = 0;
        // 外层循环控制排序的轮数，每轮找出一个最大值
        for (int i = arr.length-1; i > 0 ; i--) {
            // 内层循环用于比较相邻的两个元素，并在必要时交换它们
            for (int j = 0; j < i; j++) {
                // 如果当前元素大于下一个元素，则交换它们的位置
                if(arr[j]>arr[j+1]){
                    swap(arr,j,j+1);
                    isSwap = true;
                }
                count++;
            }
            if (!isSwap) break;
        }
        System.err.println("bubbleSort2执行次数："+count);
    }

    public static void quickSort3(int[] arr, int left, int right){
        if (left>=right) {//递归出口
            return;
        }
        int l = left;
        int r = right;
        int pivot = arr[l]; // 待排序的第一个元素作为基准值
        while (l<r) {
            while (l<r && arr[r]>=pivot) r--;   // 从右往左扫描，找到第一个比基准值小的元素
            arr[l] = arr[r];     // 找到这种元素将arr[right]放入arr[left]中
            while (l<r && arr[l]<=pivot) l++;   // 从左往右扫描，找到第一个比基准值大的元素
            arr[r] = arr[l];     // 找到这种元素将arr[left]放入arr[right]中
        }
        arr[l] = pivot; // 基准值归位
        quickSort3(arr, left, l-1);
        quickSort3(arr, r+1, right);
    }

    /**
     *
     * @param arr
     */
    public static void insertSort(int[] arr){
        //外循环，从第二个元素开始，依次与前面的元素比较, 已排序区间为 [0, i-1]
        for (int i = 1; i < arr.length; i++) {
            int base = arr[i], j = i - 1;
            // 内循环：将 base 插入到已排序区间 [0, i-1] 中的正确位置
            while (j >= 0 && arr[j] > base) {
                arr[j + 1] = arr[j];    // 将当前元素arr[j]向右移动一位
                j--;
            }
            arr[j + 1] = base;  // 将 base 插入到正确位置
        }
    }

    public static void mergeSort(int[] arr, int left, int right){
        if (left>=right) {
            return;
        }
        //临时数组
        int[] temp = new int[arr.length];
        int mid = left + (right-left)/2;
        // 对左半部分进行排序
        mergeSort(arr, left, mid);
        // 对右半部分进行排序
        mergeSort(arr, mid+1, right);
        // 合并两个已排序的子数组
        merge(arr, left, mid, right, temp);
    }

    private static void merge(int[] arr, int left, int mid, int right, int[] temp) {
        int i = left;
        int j = mid+1;
        int k = 0;
        while (i<=mid && j<=right) {
            if (arr[i]<=arr[j]) {
                temp[k++] = arr[i++];
            } else {
                temp[k++] = arr[j++];
            }
        }
        // 处理左半部分剩余的元素
        while (i<=mid) {
            temp[k++] = arr[i++];
        }
        // 处理右半部分剩余的元素
        while (j<=right) {
            temp[k++] = arr[j++];
        }
        // 将合并后的子数组复制回原数组
        for (int l = 0; l < k; l++) {
            arr[left+l] = temp[l];
        }
    }

}
