package com.example.algorithm.sort;

/**
 * Created by MintQ on 2018/7/4.
 *
 * Java中常用算法:
 *  排序：对一组数据进行从小到大（或从大到小）的顺序排列。
 *
 *  排序算法有很多种，这里介绍Java中面试经常出现的三种排序方式：冒泡、选择、快速。
 *
 *  冒泡：
        顾明思义，是气泡从液体的底部到顶部的过程，就像串糖葫芦一样，先决定最下面的数据。在算法的过程中是把一组数据从第一位开始两两比较（第1位和第2位，第2位和第3位...），选择大的值或者比较小的值交换到后面的位置。以这种方式比较第一轮后，这组数据中最大的值或者最小的就冒出来了，以此类推倒数第二、三位等。
 *
 *  选择：
 *     选择排序的方式，其实更加贴近我们正常的思考方式，就是从一组的数据的开始位置，拿出这个数据，然后依次和其他位置中数据比较，比如找最大值，只要发现后面有比其大的值就进行互换，这样第一轮下来，第一个位置上的数据就是最大值，然后从第二位依次类推。
 *
 *  快速：
 *     通过一趟排序将要排序的数据分割成独立的两部分，其中一部分的所有数据都比另外一部分的所有数据都要小，然后再按此方法对这两部分数据分别进行快速排序，整个排序过程可以递归进行，以此达到整个数据变成有序序列。
 *
 *
 */
public class SortTest {


    //冒泡
    public static void bubbleSort(int[] arr) {
		/**
         *两个for循环嵌套，
		 *外面的for循环决定一个长度为m的数据要比较多少轮才能完成排序。
		 *利用举例归类的方式，比如长度为5、8、9，需要4（第一轮：1和2，2和3，3和4，4和5；
		 *第二轮：1和2，2和3，3和4，5号位置已经上一次中排列好，不需要在参与比较；第三轮：1和2
		 *，2和3；第四轮：1和2）,7,8轮;
		 *里面的for循环决定每次一轮循环中要做多少次才能结束，这里面的规律就在长度为5的举例中，
		 *可以归类，就是4、3、2、1,,也就是它跟轮数有关系，轮数增加，比较次数就减少，比较次数
		 * n = m - i，i轮数
		 */
        for(int i = 0; i < arr.length - 1; i++) {
            for(int j = 0; j < arr.length - 1 - i; j++){
                //从小到大，大的值放后面位置。
                if (arr[j] > arr[j+1]){
                    swap(arr,j,j+1);
                }
            }
        }
    }

    private static void swap(int[] arr,int index1,int index2){
        int temp = arr[index1];
        arr[index1] = arr[index2];
        arr[index2] = temp;
    }

    //选择
    public static void selectSort(int[] arr) {
		/**
		 * 同理两个for循环，外面的for是几轮，因为最后一个不需要比较，所以是长度的m-1轮
		 * 里面的for是指每一轮比较的过程，一个就是起始位随着轮数后移，另外比较的次数也随着减少
		 * 规律同冒泡
		 */
        for(int i = 0; i < arr.length - 1; i++){
            for(int j = i + 1; j < arr.length; j++){
                if(arr[i] > arr[j]) {
                    swap(arr,i,j);
                }
            }

        }
    }


    //快速排序
    public static void quickSort(int[] arr,int low,int high) {
        int l = low;
        int h = high;
        //基数
        int povit = arr[low];
        //一趟快速排序，即l = h，这时povit的位置就固定了，左边都是小于它的值，右边都是大于它的值。
        while (l < h) {
            //从右边开始，通过高位角标h的自减，从最右位向低位逐一取出数组中的值。l < h排除了l=h情况
            while (povit < arr[h] && l < h) {
                h--;
            }
            //如果上面的循环结束，且l != h，说明右边出现小于povit元素，需要互换位置
            if (l < h) {
                swap(arr, l, h);
                l++;//从后一位开始读取数组
            }
            //从左边开始，通过低位角标l的自增，从最左边向高位逐一取出数值中的值。
            while (povit > arr[l] && l < h) {
                l++;
            }
            //如果上面的循环结束，且l != h，说明左边出现大于povit元素，需要互换位置
            if (l < h) {
                swap(arr, l, h);
                h++;
            }
            //向下继续最外面的while循环，直到l = h
        }
        //递归，povit左边继续调用quickSort
        if (l > low) {
            quickSort(arr, low, l - 1);
        }
        //递归，povit右边继续调用quickSort
        if (h < high) {
            quickSort(arr, l + 1, high);
        }

    }

    public static void main(String[] args) {
        //定义一个一维组数
        int[] arr1 = {5,8,3,9,10,55,32};
        SortTest.bubbleSort(arr1);
        printArray(arr1);
        int[] arr2 = {5,8,3,9,10,55,32};
        SortTest.selectSort(arr2);
        printArray(arr2);
        int[] arr3 = {5,8,3,9,10,55,32};
        SortTest.quickSort(arr3,0,arr3.length-1);
        printArray(arr3);

    }

    public static void printArray(int[] arr){
        String str = "[";
        for(int i = 0; i < arr.length; i++) {
            str = str + arr[i] + ",";
        }
        str = str.substring(0,str.length() - 1);
        str = str + "]";
        System.out.print(str);
        System.out.println("");
    }


}
