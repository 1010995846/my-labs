
### 🎉🎉🎉🎉🎉 欢度 “五一” 国际劳动节 （横幅先拉起来）🎉🎉🎉🎉🎉
标题一定要醒醒目目，大家五一节一定要开开心心哦 ヾ(*≧▽≦*)ﾉ

上周的打卡题「[23. 合并K个排序链表](https://leetcode-cn.com/problems/merge-k-sorted-lists/)」还历历在目，今天就来「21. 合并两个有序链表」~~送分~~(**常考**)哈哈，普普通通起个床普普通通打个卡然后普普通通出门去放个普普通通的风，舒服。（五一快乐呀 <(￣︶￣)↗GO!）

关于「合并K个有序链表」可以移步我的题解[《4 种方法详解，🤷‍♀️必须秒懂！》](https://leetcode-cn.com/problems/merge-k-sorted-lists/solution/4-chong-fang-fa-xiang-jie-bi-xu-miao-dong-by-sweet/)嗷！

---

### 合并两个有序链表

**递归解法**
代码语义太明确了，不解释 ◔ ◔
``` Java
class Solution {
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        if (l1 == null) {
            return l2;
        }
        if (l2 == null) {
            return l1;
        }
        if (l1.val <= l2.val) {
            l1.next = mergeTwoLists(l1.next, l2);
            return l1;
        }
        l2.next = mergeTwoLists(l1, l2.next);
        return l2;
    }
}
```

**迭代解法**
dummy大法有多妙，用了你就会知道! ≧◇≦
``` Java
class Solution {
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode dummyHead = new ListNode(0);
        ListNode tail = dummyHead;
        while (l1 != null && l2 != null) {
            if (l1.val < l2.val) {
                tail.next = l1;
                l1 = l1.next;
            } else {
                tail.next = l2;
                l2 = l2.next;
            }
            tail = tail.next;
        }

        tail.next = l1 == null? l2: l1;

        return dummyHead.next;
    }
}
```
这里是链表的尾插法，头插法一般用在需要链表逆序的场景，比如用迭代法解决「[206. 反转链表](https://leetcode-cn.com/problems/reverse-linked-list/)」。


---

除了「[23. 合并K个排序链表](https://leetcode-cn.com/problems/merge-k-sorted-lists/)」会用到本题「合并两个有序链表」的知识点之外，对链表进行归并排序时 merge 的部分也会用到本题知识点哦，例如「[148. 排序链表](https://leetcode-cn.com/problems/sort-list/)」。

---

大佬们随手【[关注我](https://leetcode-cn.com/u/sweetiee/)】呀～


