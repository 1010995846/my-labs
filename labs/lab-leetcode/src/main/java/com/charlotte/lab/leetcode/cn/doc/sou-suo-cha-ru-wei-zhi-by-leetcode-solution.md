### 📺 视频题解  
![35. 搜索插入的位置.mp4](340b43f1-c2f6-45cf-aa63-d0c9f78fc0c5)

### 📖 文字题解
#### 方法一：二分查找

**思路与算法**

假设题意是叫你在排序数组中寻找是否存在一个目标值，那么训练有素的读者肯定立马就能想到利用二分法在 ![O(\logn) ](./p__O_log_n__.png)  的时间内找到是否存在目标值。但这题还多了个额外的条件，即如果不存在数组中的时候需要返回按顺序插入的位置，那我们还能用二分法么？答案是可以的，我们只需要稍作修改即可。

考虑这个插入的位置 ![\textit{pos} ](./p__textit{pos}_.png) ，它成立的条件为：

![\textit{nums}\[pos-1\]<\textit{target}\le\textit{nums}\[pos\] ](./p___textit{nums}_pos-1__textit{target}le_textit{nums}_pos___.png) 

其中 ![\textit{nums} ](./p__textit{nums}_.png)  代表排序数组。由于如果存在这个目标值，我们返回的索引也是 ![\textit{pos} ](./p__textit{pos}_.png) ，因此我们可以将两个条件合并得出最后的目标：「在一个有序数组中找第一个大于等于 ![\textit{target} ](./p__textit{target}_.png)  的下标」。

问题转化到这里，直接套用二分法即可，即不断用二分法逼近查找第一个大于等于 ![\textit{target} ](./p__textit{target}_.png)  的下标 。下文给出的代码是笔者习惯的二分写法，![\textit{ans} ](./p__textit{ans}_.png)  初值设置为数组长度可以省略边界条件的判断，因为存在一种情况是 ![\textit{target} ](./p__textit{target}_.png)  大于数组中的所有数，此时需要插入到数组长度的位置。

 ![fig1](https://assets.leetcode-cn.com/solution-static/35/1.png) ![fig2](https://assets.leetcode-cn.com/solution-static/35/2.png) ![fig3](https://assets.leetcode-cn.com/solution-static/35/3.png) ![fig4](https://assets.leetcode-cn.com/solution-static/35/4.png) ![fig5](https://assets.leetcode-cn.com/solution-static/35/5.png) ![fig6](https://assets.leetcode-cn.com/solution-static/35/6.png) ![fig7](https://assets.leetcode-cn.com/solution-static/35/7.png) ![fig8](https://assets.leetcode-cn.com/solution-static/35/8.png) ![fig9](https://assets.leetcode-cn.com/solution-static/35/9.png) ![fig10](https://assets.leetcode-cn.com/solution-static/35/10.png) ![fig11](https://assets.leetcode-cn.com/solution-static/35/11.png) ![fig12](https://assets.leetcode-cn.com/solution-static/35/12.png) ![fig13](https://assets.leetcode-cn.com/solution-static/35/13.png) ![fig14](https://assets.leetcode-cn.com/solution-static/35/14.png) ![fig15](https://assets.leetcode-cn.com/solution-static/35/15.png) 

```Java [sol1-Java]
class Solution {
    public int searchInsert(int[] nums, int target) {
        int n = nums.length;
        int left = 0, right = n - 1, ans = n;
        while (left <= right) {
            int mid = ((right - left) >> 1) + left;
            if (target <= nums[mid]) {
                ans = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return ans;
    }
}
```
```C++ [sol1-C++]
class Solution {
public:
    int searchInsert(vector<int>& nums, int target) {
        int n = nums.size();
        int left = 0, right = n - 1, ans = n;
        while (left <= right) {
            int mid = ((right - left) >> 1) + left;
            if (target <= nums[mid]) {
                ans = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return ans;
    }
};
```
```JavaScript [sol1-JavaScript]
var searchInsert = function(nums, target) {
    const n = nums.length;
    let left = 0, right = n - 1, ans = n;
    while (left <= right) {
        let mid = ((right - left) >> 1) + left;
        if (target <= nums[mid]) {
            ans = mid;
            right = mid - 1;
        } else {
            left = mid + 1;
        }
    }
    return ans;
};
```

```C [sol1-C]
int searchInsert(int* nums, int numsSize, int target) {
    int left = 0, right = numsSize - 1, ans = numsSize;
    while (left <= right) {
        int mid = ((right - left) >> 1) + left;
        if (target <= nums[mid]) {
            ans = mid;
            right = mid - 1;
        } else {
            left = mid + 1;
        }
    }
    return ans;
}
```

```golang [sol1-Golang]
func searchInsert(nums []int, target int) int {
    n := len(nums)
    left, right := 0, n - 1
    ans := n
    for left <= right {
        mid := (right - left) >> 1 + left
        if target <= nums[mid] {
            ans = mid
            right = mid - 1
        } else {
            left = mid + 1
        }
    }
    return ans
}
```

**复杂度分析**

- 时间复杂度：![O(\logn) ](./p__O_log_n__.png) ，其中 *n* 为数组的长度。二分查找所需的时间复杂度为 ![O(\logn) ](./p__O_log_n__.png) 。

- 空间复杂度：*O(1)*。我们只需要常数空间存放若干变量。