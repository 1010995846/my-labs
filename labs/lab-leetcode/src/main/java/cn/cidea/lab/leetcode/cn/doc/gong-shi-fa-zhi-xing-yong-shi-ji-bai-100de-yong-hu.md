如果输入的行数是n，那么该行内容依次是：1、(n-1)/1、(n-1)(n-2)/2、(n-1)(n-2)(n-3)/3...
代码如下：
```
public static List<Integer> getRow(int rowIndex) {
        rowIndex++;
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < rowIndex; i++) {
            if (i == 0) {
                list.add(1);
            } else {
                long num = (long)list.get(i - 1) * (long)(rowIndex - i) / i;
                list.add((int)num);
            }
        }
        return list;
    }
```
