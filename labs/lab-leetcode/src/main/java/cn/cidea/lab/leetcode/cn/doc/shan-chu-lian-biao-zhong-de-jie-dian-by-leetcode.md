#### 方法：与下一个节点交换

从链表里删除一个节点 `node` 的最常见方法是修改*之前*节点的 `next` 指针，使其指向*之后*的节点。

![](https://pic.leetcode-cn.com/3579a496897df5321c110bf1301872b6e10c342f5e400ce45d2db0348d00d715-file_1555866623326)


因为，我们无法访问我们想要删除的节点 *之前* 的节点，我们始终不能修改该节点的 `next` 指针。相反，我们必须将想要删除的节点的值替换为它后面节点中的值，然后删除它之后的节点。

![](https://pic.leetcode-cn.com/858fae01d89c2080eb7e45a1f9d9a2b2f76e1a5c87815b324fd946e0bd8da495-file_1555866651920)


![](https://pic.leetcode-cn.com/902dc5d3f8c44d3cbc0b6e837711cad2eefc021fd2b9de8dfabc6d478bc779b1-file_1555866680932)


![](https://pic.leetcode-cn.com/2a6409b98dd73d6649fdc6fb984c88690547127467104c3923367be6f8fbc916-file_1555866773685)


因为我们知道要删除的节点不是列表的末尾，所以我们可以保证这种方法是可行的。


```java [juoWzFbK-Java]
public void deleteNode(ListNode node) {
    node.val = node.next.val;
    node.next = node.next.next;
}
```


**复杂度分析**

时间和空间复杂度都是：*O(1)*。