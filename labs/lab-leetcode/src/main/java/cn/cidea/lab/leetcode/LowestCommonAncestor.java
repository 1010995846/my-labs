package cn.cidea.lab.leetcode;

/**
 * Created by Charlotte on2020/3/21
 */
public class LowestCommonAncestor {

    public static void main(String[] args) {
        LowestCommonAncestor ancestor = new LowestCommonAncestor();
//        ancestor.lowestCommonAncestor();
    }

    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null) {
            return null;
        }
        if (root.val == p.val) {
            return p;
        }
        if (root.val == q.val) {
            return q;
        }
        TreeNode pn = null;
        TreeNode qn = null;
        if ((pn = lowestCommonAncestor(root.left, p, q)) != null) {
//            return pn;
        }
        if ((qn = lowestCommonAncestor(root.right, p, q)) != null) {
//            return qn;
        }
        return null;
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
