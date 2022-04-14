package cn.cidea.lab.leetcode.cn;
//删除链表中等于给定值 val 的所有节点。 
//
// 示例: 
//
// 输入: 1->2->6->3->4->5->6, val = 6
//输出: 1->2->3->4->5
// 
// Related Topics 链表 
// 👍 435 👎 0

class RemoveLinkedListElements {
    public static void main(String[] args) {
        Solution solution = new RemoveLinkedListElements().new Solution();
        ListNode node = ListNode.get(6);
        solution.removeElements(node, 6);
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
        public ListNode removeElements(ListNode head, int val) {
            if (head == null) {
                return null;
            }
            ListNode pre = null;
            ListNode cur = head;
            while (cur != null) {
                if (cur.val == val) {
                    if (pre == null) {
                        // 移除首节点
                        head = cur.next;
                    } else {
                        pre.next = cur.next;
                    }
                } else {
                    // 移除时，pre不变
                    pre = cur;
                }
                // 下一节点
                cur = cur.next;
            }
            return head;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}