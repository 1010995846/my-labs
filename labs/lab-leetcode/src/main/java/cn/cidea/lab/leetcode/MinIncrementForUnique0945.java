package cn.cidea.lab.leetcode;

/**
 * Created by Charlotte on2020/3/22
 */
public class MinIncrementForUnique0945 {


    public int minIncrementForUnique(int[] A) {
        // 排序解，若重复则+1
        // 从小到大

        int count = 0;
        // 位图，存放第i位数的数量，最坏情况全是最大数
        int[] bitmap = new int[80000];
        for (int i = 0; i < A.length; i++) {
            bitmap[A[i]]++;
        }
        // 记录重复数
        int rcount = 0;
        for (int i = 0; i < bitmap.length; i++) {
            if (bitmap[i] == 0) {
                // 有空位
                if (rcount > 0) {
                    // 存在重复
                    rcount--;
                    count += i;
                }
            } else if (bitmap[i] > 1) {
                rcount += bitmap[i] - 1;
                count -= i * (bitmap[i] - 1);
            }
        }
        return count;
    }

    public static void main(String[] args) {
        int[] ints = {3, 2, 1, 2, 1, 7};
        MinIncrementForUnique0945 ins = new MinIncrementForUnique0945();
        System.out.println(ins.minIncrementForUnique(ints));
    }
}
