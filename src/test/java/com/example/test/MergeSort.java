package com.example.test;

/**
 * Created by MintQ on 2018/5/8.
 * 八大排序（二）：归并排序
 * 归并排序（MERGE-SORT）是建立在归并操作上的一种有效的排序算法，该算法是采用分治法（Divide and Conquer）的一个非常典型的应用。分治法即将问题分(divide)成一些小的问题然后递归求解，而治(conquer)的阶段则将分的阶段得到的各答案”修补”在一起，归并排序的速度仅次于快速排序，比较次数小于快速排序的比较次数，而移动次数一般多于快速排序的移动次数。
 */
public class MergeSort {

    public float[] mergeSort(float[] array){
        float[] tempArray = new float[array.length];
        System.arraycopy(array,0,tempArray,0,array.length);
        mergeSortCore(array,tempArray,0,array.length-1);
        return tempArray;
    }

    //归并排序
    public void mergeSortCore(float[] array,float[] tempArray, int start ,int end){
        if (start == end) {
            tempArray[start] = array[start];
            return;
        } else if (start < end) {
            int middle = (start + end) >>1;
            mergeSortCore(tempArray,array,start,middle);
            mergeSortCore(tempArray,array,middle+1,end);

            int i = start;
            int j = middle +1;
            int k = start;
            while (i <= middle && j<=end) {
                if(array[i] <= array[j]){
                    tempArray[k++] = array[i++];
                } else {
                    tempArray[k++] = array[j++];
                }
            }
            while (i <= middle ) {
                tempArray[k++] = array[i++];
            }
            while (j <= end) {
                tempArray[k++] = array[j++];
            }
        }
    }

}
