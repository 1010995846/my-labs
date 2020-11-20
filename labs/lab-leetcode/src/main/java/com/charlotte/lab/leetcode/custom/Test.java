package com.charlotte.lab.leetcode.custom;

public class Test {

    public static void main(String[] args) {

//        int target = 7;
//        int[][] array = new int[][]{{1,2,7,9},{2,4,9,12},{4,8,10,13},{6,8,11,15}};
//        System.out.println(find(target, array));
//        int[] pre = new int[]{1,2,4,3,5,6};
//        int[] in = new int[]{4,2,1,5,3,6};
        int[] pre = new int[]{1, 2, 3, 4, 5, 6, 7};
        int[] in = new int[]{3, 2, 4, 1, 6, 5, 7};
        TreeNode node = reConstructBinaryTree(pre, in);
        System.out.println(node);
    }

    public static boolean find(int target, int[][] array) {
        if (array.length == 0) {
            return false;
        }
        int i = 0;
        int j = array[i].length - 1;
        /**
         * 从右下往上递增（左至右递增），当遇到大于target时，target若存在则必然位于当前位置起始的上半部，向左递减，再次判断
         */
        while (i < array.length && j >= 0) {
            int loc = array[i][j];
            if (loc == target) {
                return true;
            } else if (target < loc) {
                j--;
            } else {
                i++;
            }
        }
        return false;
    }

    public static String replaceSpace(StringBuffer str) {
        int spaceNum = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ' ') {
                spaceNum++;
            }
        }
        int newLength = str.length() + spaceNum * 2;
        int loc = str.length() - 1;
        int newLoc = newLength - 1;
        str.setLength(newLength);
        for (; loc >= 0; loc--) {
            char c = str.charAt(loc);
            if (c == ' ') {
                str.setCharAt(newLoc--, '0');
                str.setCharAt(newLoc--, '2');
                str.setCharAt(newLoc--, '%');
            } else {
                str.setCharAt(newLoc--, c);
            }
            if (loc == newLoc) {
//                剩余长度相等，即剩余字符串无空格（空格多余的长度已处理完毕）
                break;
            }
        }
        return str.toString();
    }

    public static TreeNode reConstructBinaryTree(int[] pre, int[] in) {
        if (pre.length == 0) {
            return null;
        }
        int p = pre[0];
        TreeNode node = new TreeNode(p);
        int leftSize = 0;
        for (; leftSize <= in.length; leftSize++) {
            if (p == in[leftSize]) {
                break;
            }
        }
        int rightSize = pre.length - leftSize - 1;
        int[] preLeft = new int[leftSize];
        int[] preRight = new int[rightSize];
        int[] inLeft = new int[leftSize];
        int[] inRight = new int[rightSize];
        for (int i = 0, j = 0; i < pre.length; i++) {
            if (i < leftSize) {
                preLeft[i] = pre[i + 1];
                inLeft[i] = in[i];
            } else if (i > leftSize) {
                preRight[j] = pre[i];
                inRight[j] = in[i];
                j++;
            }
        }
        node.left = reConstructBinaryTree(preLeft, inLeft);
        node.right = reConstructBinaryTree(preRight, inRight);
        return node;
    }
}

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode(int x) {
        val = x;
    }
}