package com.example.algorithm.sort;

/**
 * Created by MintQ on 2018/7/4.
 *
 * Java中常用算法:
 *  排序：对一组数据进行从小到大（或从大到小）的顺序排列。
 *
 *  排序算法有很多种，这里介绍Java中面试经常出现的三种 排序 方式：冒泡、选择、快速。
 *
 *  1、冒泡：
        顾明思义，是气泡从液体的底部到顶部的过程。在算法的过程中是把一组数据从第一位开始 两两比较（第1位和第2位，第2位和第3位...），选择大的值或者比较小的值交换到后面的位置。以这种方式比较第一轮后，这组数据中最大的值或者最小的就冒出来了，以此类推倒数第二、三位等。
 *
 *  2、选择：
 *     选择排序的方式，其实更加贴近我们正常的思考方式，就是从一组的数据的 开始位置，拿出这个数据，然后依次和其他位置中数据比较，比如找最大值，只要发现后面有比其大的值就进行互换，这样第一轮下来，第一个位置上的数据就是最大值，然后从第二位依次类推。
 *
 *  3、快速：快速排序之所以快速，是因为它使用了[分治法]。
 *     通过一趟排序将要排序的数据分割成独立的两部分，其中一部分的所有数据都比另外一部分的所有数据都要小，然后再按此方法对这两部分数据分别进行快速排序，整个排序过程可以递归进行，以此达到整个数据变成有序序列。
 *     - 快速排序是从冒泡排序演变而来的算法，但是比冒泡排序要高效很多，所以叫做快速排序。
 *
 * 《*》程序员必须知道的10大基础实用算法及其讲解
 *  一、快速排序算法：快速排序是由东尼·霍尔所发展的一种排序算法。在平均状况下，排序n个项目要Ο(nlogn)次比较。在最坏状况下则需要Ο(n2)次比较，但这种状况并不常见。事实上，快速排序通常明显比其他Ο(nlogn)算法更快，因为它的内部循环（innerloop）可以在大部分的架构上很有效率地被实现出来。
 *                  快速排序使用 分治法（Divideandconquer）策略来把一个串行（list）分为两个子串行（sub-lists）。
 *      算法步骤：1、从数列中挑出一个元素，称为“基准”（pivot），
 *              2、重新排序数列，所有元素比基准值小的摆放在基准前面，所有元素比基准值大的摆在基准的后面（相同的数可以到任一边）。在这个分区退出之后，该基准就处于数列的中间位置。这个称为分区（partition）操作。
 *              3、递归地（recursion）把小于基准值元素的子数列和大于基准值元素的子数列排序。
 *           递归的最底部情形，是数列的大小是零或一，也就是永远都已经被排序好了。虽然一直递归下去，但是这个算法总会退出，因为在每次的迭代（iteration）中，它至少会把一个元素摆到它最后的位置去。
 *  二、堆排序算法：堆排序（Heapsort）是指利用堆这种数据结构所设计的一种排序算法。堆积是一个近似完全二叉树的结构，并同时满足堆积的性质：即子结点的键值或索引总是小于（或者大于）它的父节点。
 *                堆排序的平均时间复杂度为Ο(nlogn) 。
 *      算法步骤：1、创建一个堆H[0..n-1]
 *              2、把堆首（最大值）和堆尾互换
 *              3、把堆的尺寸缩小1，并调用shift_down(0),目的是把新的数组顶端数据调整到相应位置
 *              4、重复步骤2，直到堆的尺寸为1
 *  三、归并排序：归并排序（Mergesort，台湾译作：合并排序）是建立在归并操作上的一种有效的排序算法。该算法是采用 分治法（DivideandConquer）的一个非常典型的应用。
 *      算法步骤：1、申请空间，使其大小为两个已经排序序列之和，该空间用来存放合并后的序列
 *              2、设定两个指针，最初位置分别为两个已经排序序列的起始位置
 *              3、比较两个指针所指向的元素，选择相对小的元素放入到合并空间，并移动指针到下一位置
 *              4、重复步骤3直到某一指针达到序列尾
 *              5、将另一序列剩下的所有元素直接复制到合并序列尾
 *  四、二分查找算法：二分查找算法是一种在 有序数组 中查找某一特定元素的搜索算法。搜素过程从数组的中间元素开始，如果中间元素正好是要查找的元素，则搜素过程结束；如果某一特定元素大于或者小于中间元素，则在数组大于或小于中间元素的那一半中查找，而且跟开始一样从中间元素开始比较。
 *                  如果在某一步骤数组为空，则代表找不到。这种搜索算法每一次比较都使搜索范围缩小一半。折半搜索每次把搜索区域减少一半，时间复杂度为Ο(logn) 。
 *
 *  五、BFPRT(线性查找算法)：BFPRT算法解决的问题十分经典，即从某n个元素的序列中选出第k大（第k小）的元素，通过巧妙的分析，BFPRT可以保证在最坏情况下仍为线性时间复杂度。该算法的思想与快速排序思想相似，当然，为使得算法在最坏情况下，依然能达到o(n)的时间复杂度，五位算法作者做了精妙的处理。
 *
 *  六、DFS（深度优先搜索）：深度优先搜索算法（Depth-First-Search），是搜索算法的一种。它沿着树的深度遍历树的节点，尽可能深的搜索树的分支。
 *
 *  七、BFS(广度优先搜索)：广度优先搜索算法（Breadth-First-Search），是一种图形搜索算法。简单的说，BFS是从根节点开始，沿着树(图)的宽度遍历树(图)的节点。如果所有节点均被访问，则算法中止。BFS同样属于盲目搜索。一般用队列数据结构来辅助实现BFS算法。
 *
 *  八、Dijkstra算法：戴克斯特拉算法（Dijkstra’salgorithm）是由荷兰计算机科学家艾兹赫尔·戴克斯特拉提出。迪科斯彻算法使用了广度优先搜索解决非负权有向图的单源最短路径问题，算法最终得到一个最短路径树。该算法常用于路由算法或者作为其他图算法的一个子模块。
 *
 *  九、动态规划算法：动态规划（Dynamicprogramming）是一种在数学、计算机科学和经济学中使用的，通过把原问题分解为相对简单的子问题的方式求解复杂问题的方法。动态规划常常适用于有重叠子问题和最优子结构性质的问题，动态规划方法所耗时间往往远少于朴素解法。
 *
 *  十、朴素贝叶斯分类算法：朴素贝叶斯分类算法是一种基于贝叶斯定理的简单概率分类算法。
 *                      贝叶斯分类的基础是概率推理，就是在各种条件的存在不确定，仅知其出现概率的情况下，如何完成推理和决策任务。概率推理是与确定性推理相对应的。而朴素贝叶斯分类器是基于独立假设的，即假设样本每个特征与其他特征都不相关。
 *
 */
public class SortTest {


    //冒泡
    public static void bubbleSort(int[] arr) {
		/**
         * 两个for循环嵌套，
		 * 外面的for循环 决定一个长度为m的数据要 比较多少轮 才能完成排序。 这里的 m = arr.length
		 * 利用举例归类的方式，比如长度为5，需要4轮（第一轮：1和2，2和3，3和4，4和5；
		 * 第二轮：1和2，2和3，3和4，5号位置已经上一次中排列好，不需要在参与比较；
         * 第三轮：1和2，2和3；
         * 第四轮：1和2）;
		 * 里面的for循环 决定每次一轮循环中要做多少次才能结束，这里面的规律就在长度为5的举例中，
		 * 可以归类，就是4、3、2、1,,也就是它跟轮数有关系，轮数增加，比较次数就减少，比较次数n = m - i，i轮数
		 */
        for(int i = 0; i < arr.length - 1; i++) {
            // 此处你可能会疑问的j<n-i-1，因为冒泡是把每轮循环中较大的数飘到后面,数组下标又是从0开始的，i下标后面已经排序的个数就得多减1，
            // 总结就是i增多少，j的循环位置减多少,所以 j < arr.length - 1 - i
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
		 * 里面的for是指每一轮比较的过程，1、一个就是起始位随着轮数后移，2、另外比较的次数也随着减少
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
        int pivot = arr[low];
        //一趟快速排序，即l = h，这时povit的位置就固定了，左边都是小于它的值，右边都是大于它的值。
        while (l < h) {
            //从右边开始，通过高位角标h的自减，从最右位向低位逐一取出数组中的值。l < h排除了l=h情况
            while (pivot < arr[h] && l < h) {
                h--;
            }
            //如果上面的循环结束，且l != h，说明右边出现小于povit元素，需要互换位置
            if (l < h) {
                swap(arr, l, h);
                l++;//从后一位开始读取数组
            }
            //从左边开始，通过低位角标l的自增，从最左边向高位逐一取出数值中的值。
            while (pivot > arr[l] && l < h) {
                l++;
            }
            //如果上面的循环结束，且l != h，说明左边出现大于povit元素，需要互换位置
            if (l < h) {
                swap(arr, l, h);
                h--;
            }
            //向下继续最外面的while循环，直到l = h
        }
        //递归，povit左边继续调用quickSort
        if (l > low) {
            quickSort(arr, low, l - 1);
        }
        //递归，povit右边继续调用quickSorth
        if (h < high) {
            quickSort(arr,  l+ 1, high);
        }

    }

    /**
     * @author  xiaoguozi
     * @create  2018/7/15 下午6:37
     * @desc    快速排序 - 指针交换法
     * 要点：1、我们首先选定基准元素Pivot，并且设置两个指针left（low）和right（high），指向数列的最左和最右两个元素：
     *      2、接下来是第一次循环，从right指针开始，把指针所指向的元素和基准元素做比较。如果大于等于pivot，则指针向左移动；如果小于pivot，则right指针停止移动，切换到left指针。
     *      3、轮到left指针行动，把指针所指向的元素和基准元素做比较。如果小于等于pivot，则指针向右移动；如果大于pivot，则left指针停止移动。交换元素。
     *      4、重复2、3步骤，直到left和right指针已经重合在了一起。当left和right指针重合之时，我们让pivot元素和left与right重合点的元素进行交换。
     **/
    public static void quickSort_2(int[] arr , int low , int high) {
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

    public static void quickSort_3(int[] arr , int leftIndex , int rightIndex) {
        int left = leftIndex;
        int right = rightIndex;
        // 待排序的第一个元素作为基准值
        int key = arr[left];
        // 从左右两边交替扫描，直到left = right
        while (left < right) {
            while (arr[right] >= key && right > left) {
            // 从右往左扫描，找到第一个比基准值小的元素
                right--;
            }
            // 找到这种元素将arr[right]放入arr[left]中
            arr[left] = arr[right];
            while (arr[left] <= key && left < right) {
            // 从左往右扫描，找到第一个比基准值大的元素
                left++;
            }
            // 找到这种元素将arr[left]放入arr[right]中
            arr[right] = arr[left];
        }
        // 基准值归位
        arr[left] = key;
        // 对基准值左边的元素进行递归排序
        quickSort(arr, leftIndex, left - 1);
        // 对基准值右边的元素进行递归排序。
        quickSort(arr, right + 1, rightIndex);
    }

    /**
     * 插入排序
     * @author 溪云阁
     * @param arr
     * @return int[]
     * 一组无序序列{A1,A2,........An}
     * 先取出A1，然后从A2与A1比较，比较完之后序列状况是{A1,A2}{A3..........An}，这时候其中{A1,A2}就变成有序
     * 然后取出A3 ，放到{A1,A2}有序序列合适位置，从而形成{A1,A2,A3}{A4........An}
     * 重复这个过程，直到取出An放入{A1,A2........An-1}有序序列中
     */
    public static int[] insertSort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            // 保存每次需要插入的那个数
            int temp = arr[i];
            int j;
            for (j = i; j > 0 && arr[j - 1] > temp; j--) {
            // 把大于需要插入的数往后移动。最后不大于temp的数就空出来j
                arr[j] = arr[j - 1];
            }
            // 将需要插入的数放入这个位置
            arr[j] = temp;
        }
        return arr;
    }

    /**
     * 两路归并算法，两个排好序的子序列合并为一个子序列
     * @author 溪云阁
     * @param a
     * @param left
     * @param mid
     * @param right void
     */
    public static void merge(int[] a, int left, int mid, int right) {
        // 辅助数组
        final int[] temp = new int[a.length];
        // i、j是检测指针，k是存放指针
        int i = left, j = mid + 1, k = 0;
        while (i <= mid && j <= right) {
            if (a[i] <= a[j]) {
                temp[k++] = a[i++];
            } else {
                temp[k++] = a[j++];
            }
        }
        // 如果第一个序列未检测完，直接将后面所有元素加到合并的序列中
        while (i <= mid) temp[k++] = a[i++];
        while (j <= right)  temp[k++] = a[j++]; // 同上
        k = 0;
        //将temp中的元素全部拷贝到原数组中
        while(left <= right){
            a[left++] = temp[k++];
        }
    }
    /**
     * 归并排序
     * @author 溪云阁
     * @param a
     * @param start
     * @param end void
     */
    public static void mergeSort(int[] a, int start, int end) {
        // 当子序列中只有一个元素时结束递归
        if (start < end) {
            // 划分子序列
            int mid = start + (end - start) / 2;    //防止整数越界
            // 对左侧的序列进行递归排序
            mergeSort(a, start, mid);
            // 对右侧的序列进行递归排序
            mergeSort(a, mid + 1, end);
            // 合并
            merge(a, start, mid, end);
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
        int[] arr4 = {5,8,3,9,10,55,32};
        SortTest.quickSort_3(arr4,0,arr3.length-1);
        printArray(arr4);
        int[] arr5 = {5,8,3,9,10,55,32};
        SortTest.insertSort(arr5);
        printArray(arr5);
        int[] arr6 = {5,8,3,9,10,55,32};
        SortTest.mergeSort(arr6,0, arr6.length-1);
        printArray(arr6);

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
