package com.example.algorithm.sort;

/**
 * Created by MintQ on 2018/7/17.
 */
public abstract class Sort<T extends Comparable<T>>  {

    public abstract void sort(T[] nums);

    protected boolean less(T v, T w) {
        return v.compareTo(w) < 0;
    }

    protected void swap(T[] a, int i, int j) {
        T t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

}
