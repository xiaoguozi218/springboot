package com.example.algorithm.lookup;

/**
 * @author  xiaoguozi
 * @create  2018/7/13 下午8:25
 * @desc
 *
 * 一、二分查找算法（JAVA）
 *   1.二分查找又称 折半查找，它是一种效率较高的查找方法。
 *   2.二分查找要求：（1）必须采用顺序存储结构 （2）必须按关键字大小有序排列
 *   3.原理：将数组分为三部分，依次是中值（所谓的中值就是数组中间位置的那个值）前，中值，中值后；将要查找的值和数组的中值进行比较，若小于中值则在中值前 面找，若大于中值则在中值后面找，等于中值时直接返回。
 *          然后依次是一个递归过程，将前半部分或者后半部分继续分解为三部分。
 *   4.实现：二分查找的实现用 递归 和 循环 两种方式
 *
 *
 **/
public class BinarySearch {


    /*
     * 循环实现二分查找算法 arr 已排好序的数组、 x 需要查找的数、 -1 无法查到数据
     */
    public static int binarySearch(int[] arr, int x) {
        int low = 0;
        int high = arr.length-1;
        while(low <= high) {
            int middle = (low + high)/2;
            if(x == arr[middle]) {
                return middle;
            }else if(x <arr[middle]) {
                high = middle - 1;
            }else {
                low = middle + 1;
            }
        }
        return -1;
    }

    //递归实现二分查找
    public static int binarySearch(int[] arr,int target,int left,int right){
        int mid = (left + right)/2;
        if(target < arr[left] || target > arr[right] || left > right){
            return -1;
        }
        if (target < arr[mid]) {
            return binarySearch(arr,target,left,mid-1);
        } else if (target > arr[mid]){
            return binarySearch(arr,target,mid+1,right);
        } else {
            return mid;
        }
    }

    public static void main(String[] args) {
        int[] arr = { 6, 12, 33, 87, 90, 97, 108, 561 };
        System.out.println("循环查找:" + binarySearch(arr, 87));
        System.out.println("递归查找:" + binarySearch(arr,87,3,arr.length-1));
    }
}
