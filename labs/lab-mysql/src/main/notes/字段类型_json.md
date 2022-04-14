MySQL 5.7开始支持JSON类型字段； 8.0 版本解决了更新 JSON 的日志性能瓶颈，支持索引。

查询

```mysql
SELECT 
    userId,
    loginInfo->>"$.cellphone" cellphone,
    loginInfo->>"$.wxchat" wxchat
FROM UserLogin;
```

索引

创建了一个虚拟列 cellphone，这个列是由函数 loginInfo->>"$.cellphone" 计算得到的。然后在这个虚拟列上创建一个唯一索引 idx_cellphone

```mysql
ALTER TABLE UserLogin ADD COLUMN cellphone VARCHAR(255) AS (loginInfo->>"$.cellphone");

ALTER TABLE UserLogin ADD UNIQUE INDEX idx_cellphone(cellphone);
```



**JSON 类型比较适合存储一些修改较少、相对静态的数据**

- 使用 JSON 数据类型，推荐用 MySQL 8.0.17 以上的版本，性能更好，同时也支持 Multi-Valued Indexes；
- JSON 数据类型的好处是无须预先定义列，数据本身就具有很好的描述性；
- 不要将有明显关系型的数据用 JSON 存储，如用户余额、用户姓名、用户身份证等，这些都是每个用户必须包含的数据；

> 项目教训：严格遵循隔离原则，区分字段，不要把JSON弄成不分类的垃圾堆

- JSON 数据类型推荐使用在不经常更新的静态数据存储。