package com.wonders.util;

/**
 * Created by bjy on 2018/3/16.
 */

public class SortUtil {

    /**
     * 快速排序
     * @param array
     * @param point
     * @param left
     * @param right
     */
    public static void quickSort(String[] array, int point, int left, int right){
        if (left >= right)
            return;

        int index_left = left;
        int index_right = right;

        //获取第一轮的分割点
        int index = partition(array, point, index_left, index_right);

        //排序前半部分
        quickSort(array, ((index - 1) + left)/2, left, index-1);

        //排序后半部分
        quickSort(array, (right + (index + 1))/2, index+1, right);
    }

    public static int partition(String[] array, int point, int left, int right){
        //获取基准点
        String key = array[point];

        while (left < right){
            //从后往前扫描
            while (array[right].compareTo(key) >= 0 && right >left){
                right--;
            }
            array[left] = array[right];

            //从前往后扫描
            while (array[left].compareTo(key) <= 0 && left < right){
                left++;
            }
            array[right] = array[left];
        }
        //此时 left = right
        array[right] = key;
        return right;
    }
}
