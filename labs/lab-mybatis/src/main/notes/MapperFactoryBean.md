MapperFactoryBean

两个核心

初始化afterPropertiesSet

```java
// class DaoSupport implements InitializingBean
@Override
public final void afterPropertiesSet() throws IllegalArgumentException, BeanInitializationException {
    // Let abstract subclasses check their configuration.
    checkDaoConfig();

    // Let concrete implementations initialize themselves.
    try {
        initDao();
    }
    catch (Exception ex) {
        throw new BeanInitializationException("Initialization of DAO failed", ex);
    }
}
```





```java
// class MapperFactoryBean<T> extends SqlSessionDaoSupport implements FactoryBean<T>
@Override
protected void checkDaoConfig() {
    // notNull(this.sqlSessionTemplate, "Property 'sqlSessionFactory' or 'sqlSessionTemplate' are required");
    super.checkDaoConfig();

    notNull(this.mapperInterface, "Property 'mapperInterface' is required");

    // private SqlSessionTemplate sqlSessionTemplate;
    Configuration configuration = getSqlSession().getConfiguration();
    if (this.addToConfig && !configuration.hasMapper(this.mapperInterface)) {
        try {
            // 添加Mapper
            configuration.addMapper(this.mapperInterface);
        } catch (Exception e) {
            logger.error("Error while adding the mapper '" + this.mapperInterface + "' to configuration.", e);
            throw new IllegalArgumentException(e);
        } finally {
            ErrorContext.instance().reset();
        }
    }
}
```

> configuration.addMapper解析转《Mapper》



获取Mapper实例

FactoryBean#getObject()

```java
// class MapperFactoryBean<T> extends SqlSessionDaoSupport implements FactoryBean<T>
@Override
public T getObject() throws Exception {
  return getSqlSession().getMapper(this.mapperInterface);
}
```

> configuration.getMapper解析转《Mapper》