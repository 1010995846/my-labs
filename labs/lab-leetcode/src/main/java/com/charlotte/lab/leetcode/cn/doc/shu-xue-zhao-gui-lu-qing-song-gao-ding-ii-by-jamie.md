![1111.png](https://pic.leetcode-cn.com/1598229879-eGNcEZ-1111.png)


### 解题思路
思路和https://leetcode-cn.com/problems/pascals-triangle/solution/shu-xue-zhao-gui-lu-qing-song-gao-ding-by-jamieche/是一样的

### 代码

```python3
class Solution:
    def getRow(self, rowIndex: int) -> List[int]:
        if rowIndex==0:
            return [1]
        elif rowIndex==1:
            return [1,1]
        else:
            a=[1,1]
            for i in range(1,rowIndex+1):
                c=[]
                c.append(1)
                for j in range(1,i):
                    c.append(a[j-1]+a[j])
                c.append(1)
                a=c
            return a
```