package com.charlotte.lab.leetcode;

import javax.xml.soap.Node;
import java.util.*;

/**
 * PASS
 * <p>
 * Given a binary tree, return the inorder traversal of its nodes' values.
 */
public class No0094_BinaryTreeInorderTraversal {

    public static void main(String[] args) {

    }

    public static List<Integer> inorderTraversal(TreeNode root) {

        List<Integer> list = new ArrayList<>();
        if (root == null) {
            return list;
        }

        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            while (stack.peek().left != null) {
//                左入栈
                stack.push(stack.peek().left);
            }
            while (!stack.isEmpty()) {
                TreeNode cur = stack.pop();
                list.add(cur.val);

                if (cur.right != null) {
//                    右入栈
                    stack.push(cur.right);
                    break;
                }
            }

        }

        return list;
    }


    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }
}
