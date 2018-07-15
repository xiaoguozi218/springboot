package com.example.test.interview;
/**
 * @author  xiaoguozi
 * @create  2018/7/15 下午6:13
 * @desc    算法练习
 **/
public class ArithmeticTest {
    /**
     * @author  xiaoguozi
     * @create  2018/7/15 下午6:32
     * @desc    冒泡排序
     **/
    public static void bubbleSort(int[] arr) {
        for (int i = 0; i < arr.length - 1 ; i++) {
            for (int j = 0; j < arr.length-1-i ; j++) {
                if (arr[j] > arr[j+1]) {
                    swap(arr,j,j+1);
                }
            }
        }
    }

    /**
     * @author  xiaoguozi
     * @create  2018/7/15 下午6:33
     * @desc    选择排序
     **/
    public static void selectSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = i+1; j < arr.length; j++) {
                if (arr[i] > arr[j]) {
                    swap(arr,i,j);
                }
            }
        }
    }

    /**
     * @author  xiaoguozi
     * @create  2018/7/15 下午6:37
     * @desc    快速排序
     **/
    public static void quickSort(int[] arr , int low , int high) {
        if (low > high) {
            return;
        }
        int l = low;
        int h = high;
        int pivot = arr[low];   //基数  定义基准值为数组第一个数
        while (l < h) {
            //从右边开始
            while (pivot <= arr[h] && l < h) {  //从右往左找比基准值小的数
                h--;
            }
            //左边
            while (pivot >= arr[l] && l < h) {  //从左往右找比基准值大的数
                l++;
            }
            if (l<h) {                          //如果l<h，交换它们
                swap(arr,l,h);
            }
            arr[low] = arr[l];
            arr[l] = pivot;               //把基准值放到合适的位置
            quickSort(arr,low,l-1); //对左边的子数组进行快速排序
            quickSort(arr,l+1,high); //对右边的子数组进行快速排序
        }

    }

    private static void swap(int[] arr, int j, int i) {
        int temp =arr[j];
        arr[j] = arr[i];
        arr[i] = temp;
    }


    public static void main(String[] args) {
        int[] arr = {3,5,2,8,6,9,1};
//        bubbleSort(arr);
//        selectSort(arr);
        quickSort(arr,0,arr.length-1);
        for (int i = 0; i < arr.length; i++) {
            System.err.print(i+",");
        }
    }

}
