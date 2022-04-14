# 语法

- `limit M,N`

- `limit N`

`M`偏移量，`N`数据量。

# 场景

M过大会扫描大量无用数据，导致慢查

![limit](\picture\limit.jpg)

**方案：**

- 使用索引覆盖+子查询索引

  ```mysql
  select a.* from table a
  LEFT ...
  where a.id >= (
      select id from table 
      WHERE ... 
  	order by id
      limit 1000000,1)
  order by a.id limit 25
  ```

  提前用子查询limit出索引偏移范围，防止无用列的数据。

  - 查询与子查询的order by条件需相同；

  > 问题1：若查询条件复杂，子查询依旧要扫描对应查询条件的列数据。

- 重定义起始位置

  传入上页的最后索引来代替子查询索引

  ```mysql
  select a.* from table a
  LEFT ...
  where a.id > #{lastMaxId}
  AND ...
  order by a.id limit 25
  ```

  - 需要知道前一页的最后ID，不能跳页；
  - order by条件与起始索引列需相同；

- 降级

	配置limit的偏移量和获取数一个最大值，超过这个最大值，就返回空数据。
	
	实际环境超过这个值已经不是在分页了，而是在刷数据，如果确认要找数据，应该输入合适条件来缩小范围，而不是一页一页分页。

