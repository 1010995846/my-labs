package cn.cidea.lab.leetcode.cn;
//将两个升序链表合并为一个新的 升序 链表并返回。新链表是通过拼接给定的两个链表的所有节点组成的。 
//
// 
//
// 示例： 
//
// 输入：1->2->4, 1->3->4
//输出：1->1->2->3->4->4
// 
// Related Topics 链表 
// 👍 1239 👎 0

class MergeTwoSortedLists {
    public static void main(String[] args) {
        Solution solution = new MergeTwoSortedLists().new Solution();
        solution.mergeTwoLists(ListNode.get(3), ListNode.get(4));
    }

//leetcode submit region begin(Prohibit modification and deletion)

    /**
     * Definition for singly-linked list.
     * public class ListNode {
     * int val;
     * ListNode next;
     * ListNode() {}
     * ListNode(int val) { this.val = val; }
     * ListNode(int val, ListNode next) { this.val = val; this.next = next; }
     * }
     */
    class Solution {
        public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
            ListNode head = null;
            ListNode node = head;
            while (l1 != null || l2 != null) {
                ListNode t = null;
                if (l1 == null) {
                    t = l2;
                    l2 = l2.next;
                } else if (l2 == null) {
                    t = l1;
                    l1 = l1.next;
                } else if (l1.val < l2.val) {
                    t = l1;
                    l1 = l1.next;
                } else {
                    t = l2;
                    l2 = l2.next;
                }
                if (head == null) {
                    head = t;
                    node = head;
                } else {
                    node.next = t;
                    node = t;
                }
            }
            return head;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}