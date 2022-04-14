package cn.cidea.lab.leetcode.cn;
//给定一个链表，删除链表的倒数第 n 个节点，并且返回链表的头结点。 
//
// 示例： 
//
// 给定一个链表: 1->2->3->4->5, 和 n = 2.
//
//当删除了倒数第二个节点后，链表变为 1->2->3->5.
// 
//
// 说明： 
//
// 给定的 n 保证是有效的。 
//
// 进阶： 
//
// 你能尝试使用一趟扫描实现吗？ 
// Related Topics 链表 双指针 
// 👍 966 👎 0

class RemoveNthNodeFromEndOfList {
    public static void main(String[] args) {
        Solution solution = new RemoveNthNodeFromEndOfList().new Solution();

        solution.removeNthFromEnd(ListNode.get(5), 2);
    }

//leetcode submit region begin(Prohibit modification and deletion)

    /**
     * Definition for singly-linked list.
     * public class ListNode {
     * int val;
     * ListNode next;
     * ListNode(int x) { val = x; }
     * }
     */
    class Solution {
        public ListNode removeNthFromEnd(ListNode head, int n) {
            ListNode pre = null;
            ListNode tail = head;
            // 以tail为尾节点时，倒数序号，初始为1
            int i = 1;
            while (tail.next != null) {
                if (n == i) {
                    // 当倒数序号达到目标时，target同步移动
                    if (pre == null) {
                        pre = head;
                    } else {
                        pre = pre.next;
                    }
                } else {
                    // target不移动，倒数序号+1
                    i++;
                }
                tail = tail.next;
            }
            /**
             * 1、头节点
             * 2、非头节点
             */
            if (pre == null) {
                head = head.next;
            } else {
                pre.next = pre.next.next;
            }
            return head;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}