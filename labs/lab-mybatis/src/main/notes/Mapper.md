# getMapper

时序图

![config](picture\config.webp)

Mapper通过SqlSession接口获取实例

```java
public interface SqlSession extends Closeable {

    /**
     * Retrieves a mapper.
     * @param <T> the mapper type
     * @param type Mapper interface class
     * @return a mapper bound to this SqlSession
     */
    <T> T getMapper(Class<T> type);
	...
}
```

默认实现

```java
public class DefaultSqlSession implements SqlSession {
    private final Configuration configuration;

    public DefaultSqlSession(Configuration configuration, Executor executor, boolean autoCommit) {
        this.configuration = configuration;
        this.executor = executor;
        this.dirty = false;
        this.autoCommit = autoCommit;
    }

    @Override
    public <T> T getMapper(Class<T> type) {
        return configuration.getMapper(type, this);
    }
    ...
}
```

Configuration，包含mybatis各类配置的配置中心

```java
public class Configuration {

    protected final MapperRegistry mapperRegistry = new MapperRegistry(this);

    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return mapperRegistry.getMapper(type, sqlSession);
    }
	...
}
```

MapperRegistry，Mapper注册信息

```java
public class MapperRegistry {

    private final Map<Class<?>, MapperProxyFactory<?>> knownMappers = new HashMap<>();

    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        final MapperProxyFactory<T> mapperProxyFactory = (MapperProxyFactory<T>) knownMappers.get(type);
        if (mapperProxyFactory == null) {
            throw new BindingException("Type " + type + " is not known to the MapperRegistry.");
        }
        try {
            return mapperProxyFactory.newInstance(sqlSession);
        } catch (Exception e) {
            throw new BindingException("Error getting mapper instance. Cause: " + e, e);
        }
    }
    ...
}
```

最终从MapperRegistry中获取到MapperProxyFactory，MapperProxyFactory.newInstance()生产mapper实例

MapperProxyFactory

```java
public class MapperProxyFactory<T> {

    private final Class<T> mapperInterface;
    private final Map<Method, MapperMethodInvoker> methodCache = new ConcurrentHashMap<>();

    public MapperProxyFactory(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    public Class<T> getMapperInterface() {
        return mapperInterface;
    }

    public Map<Method, MapperMethodInvoker> getMethodCache() {
        return methodCache;
    }

    @SuppressWarnings("unchecked")
    protected T newInstance(MapperProxy<T> mapperProxy) {
        return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[] { mapperInterface }, mapperProxy);
    }

    public T newInstance(SqlSession sqlSession) {
        final MapperProxy<T> mapperProxy = new MapperProxy<>(sqlSession, mapperInterface, methodCache);
        return newInstance(mapperProxy);
    }

}
```

MapperProxy

> TODO

## knownMappers

接下来的问题就是MapperRegistry.knownMappers是如何配置完成的。

看看SqlSessionFactoryBuilder初始化Configuration的过程

```java
public class SqlSessionFactoryBuilder {

    public SqlSessionFactory build(InputStream inputStream, String environment, Properties properties) {
        try {
            // 读取xml
            XMLConfigBuilder parser = new XMLConfigBuilder(inputStream, environment, properties);
            // parse()生成Configuration配置实例
            return build(parser.parse());
        } catch (Exception e) {
            throw ExceptionFactory.wrapException("Error building SqlSession.", e);
        } finally {
            ErrorContext.instance().reset();
            try {
                inputStream.close();
            } catch (IOException e) {
                // Intentionally ignore. Prefer previous error.
            }
        }
    }

    public SqlSessionFactory build(Configuration config) {
        return new DefaultSqlSessionFactory(config);
    }

}
```

XMLConfigBuilder.parse()生成Configuration配置实例

```java
public class XMLConfigBuilder extends BaseBuilder {

    public Configuration parse() {
        if (parsed) {
            throw new BuilderException("Each XMLConfigBuilder can only be used once.");
        }
        parsed = true;
        parseConfiguration(parser.evalNode("/configuration"));
        return configuration;
    }

    private void parseConfiguration(XNode root) {
        try {
            // issue #117 read properties first
            propertiesElement(root.evalNode("properties"));
            Properties settings = settingsAsProperties(root.evalNode("settings"));
            loadCustomVfs(settings);
            loadCustomLogImpl(settings);
            typeAliasesElement(root.evalNode("typeAliases"));
            pluginElement(root.evalNode("plugins"));
            objectFactoryElement(root.evalNode("objectFactory"));
            objectWrapperFactoryElement(root.evalNode("objectWrapperFactory"));
            reflectorFactoryElement(root.evalNode("reflectorFactory"));
            settingsElement(settings);
            // read it after objectFactory and objectWrapperFactory issue #631
            environmentsElement(root.evalNode("environments"));
            databaseIdProviderElement(root.evalNode("databaseIdProvider"));
            typeHandlerElement(root.evalNode("typeHandlers"));
            mapperElement(root.evalNode("mappers"));
        } catch (Exception e) {
            throw new BuilderException("Error parsing SQL Mapper Configuration. Cause: " + e, e);
        }
    }
    ...
}
```

parseConfiguration()方法加载了各类配置，转到加载mappers的`mapperElement()`

```java
// XMLConfigBuilder.class
private void mapperElement(XNode parent) throws Exception {
    if (parent != null) {
        for (XNode child : parent.getChildren()) {
            // 解析package方式
            if ("package".equals(child.getName())) {
                String mapperPackage = child.getStringAttribute("name");
                configuration.addMappers(mapperPackage);
            } else {
                String resource = child.getStringAttribute("resource");
                String url = child.getStringAttribute("url");
                String mapperClass = child.getStringAttribute("class");
                if (resource != null && url == null && mapperClass == null) {
                    // 解析resource全限定xml
                    ErrorContext.instance().resource(resource);
                    try(InputStream inputStream = Resources.getResourceAsStream(resource)) {
                        XMLMapperBuilder mapperParser = new XMLMapperBuilder(inputStream, configuration, resource, configuration.getSqlFragments());
                        mapperParser.parse();
                    }
                } else if (resource == null && url != null && mapperClass == null) {
                    // 解析 url 配置本地或远程xml
                    ErrorContext.instance().resource(url);
                    try(InputStream inputStream = Resources.getUrlAsStream(url)){
                        XMLMapperBuilder mapperParser = new XMLMapperBuilder(inputStream, configuration, url, configuration.getSqlFragments());
                        mapperParser.parse();
                    }
                } else if (resource == null && url == null && mapperClass != null) {
                    // 解析Mapper接口路径
                    Class<?> mapperInterface = Resources.classForName(mapperClass);
                    configuration.addMapper(mapperInterface);
                } else {
                    throw new BuilderException("A mapper element may only specify a url, resource or class, but not more than one.");
                }
            }
        }
    }
}
```

mapperParser.parse()解析xml的mapper

```java
// XMLMapperBuilder.class
public void parse() {
    if (!configuration.isResourceLoaded(resource)) {
        configurationElement(parser.evalNode("/mapper"));
        configuration.addLoadedResource(resource);
        // 绑定mapper
        bindMapperForNamespace();
    }
	// 分别取出configuration中的resultMap、cacheRef、statement配置，执行一下resolve()
    parsePendingResultMaps();
    parsePendingCacheRefs();
    parsePendingStatements();
}

private void bindMapperForNamespace() {
    String namespace = builderAssistant.getCurrentNamespace();
    if (namespace != null) {
        Class<?> boundType = null;
        try {
            boundType = Resources.classForName(namespace);
        } catch (ClassNotFoundException e) {
            // ignore, bound type is not required
        }
        if (boundType != null && !configuration.hasMapper(boundType)) {
            // Spring may not know the real resource name so we set a flag
            // to prevent loading again this resource from the mapper interface
            // look at MapperAnnotationBuilder#loadXmlResource
            configuration.addLoadedResource("namespace:" + namespace);
            configuration.addMapper(boundType);
        }
    }
}

```

无论何种情况，最终都是调用configuration.addMapper()添加mapper

```java
// Configuration.class
public <T> void addMapper(Class<T> type) {
    mapperRegistry.addMapper(type);
}
```

mapperRegistry.addMapper(type)

```java
// MapperRegistry.class
public <T> void addMapper(Class<T> type) {
    if (type.isInterface()) {
        if (hasMapper(type)) {
            throw new BindingException("Type " + type + " is already known to the MapperRegistry.");
        }
        boolean loadCompleted = false;
        try {
            // 注册mapper
            knownMappers.put(type, new MapperProxyFactory<>(type));
            // It's important that the type is added before the parser is run
            // otherwise the binding may automatically be attempted by the
            // mapper parser. If the type is already known, it won't try.
            MapperAnnotationBuilder parser = new MapperAnnotationBuilder(config, type);
            parser.parse();
            loadCompleted = true;
        } finally {
            if (!loadCompleted) {
                knownMappers.remove(type);
            }
        }
    }
}
```

MapperAnnotationBuilder.parse()是解析Mapper接口上的@Select等注解。

```java
// MapperAnnotationBuilder.class
public void parse() {
    String resource = type.toString();
    if (!configuration.isResourceLoaded(resource)) {
		// mapper接口才会再次解析xml
        loadXmlResource();
        configuration.addLoadedResource(resource);
        assistant.setCurrentNamespace(type.getName());
        parseCache();
        parseCacheRef();
        for (Method method : type.getMethods()) {
            if (!canHaveStatement(method)) {
                continue;
            }
            if (getAnnotationWrapper(method, false, Select.class, SelectProvider.class).isPresent()
                && method.getAnnotation(ResultMap.class) == null) {
                // 解析resultMap，添加到configuration的mappedStatements的resultMaps
                parseResultMap(method);
            }
            try {
                // 解析mapper接口中的所有方法。并存到configuration的mappedStatements
                // mappedStatements是StrictMap，一个不允许key重复的Map
                parseStatement(method);
            } catch (IncompleteElementException e) {
                configuration.addIncompleteMethod(new MethodResolver(this, method));
            }
        }
    }
    parsePendingMethods();
}
```

同一个value会存储2次，**一个全限定名作为key，另一个就是只用方法名(sql语句的id)来作为key**

> 为了兼容早期直接通过方法名调用







### plus

mybatis-plus重写了mybatis的一些类，如MybatisConfiguration

在MybatisPlusAutoConfiguration中用MybatisConfiguration代替Configuration设置进了MybatisSqlSessionFactoryBean，从而取代上文解析到的一系列操作。

> MybatisPlusAutoConfiguration是一个spring-starter自启动类，本文不分析启动原理，只考虑初始化过程。

MybatisConfiguration继承自Configuration。大体没变，多了一些自动化的配置。



```java
// MybatisPlusAutoConfiguration.class
@Bean
@ConditionalOnMissingBean
public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
    // TODO 使用 MybatisSqlSessionFactoryBean 而不是 SqlSessionFactoryBean
    MybatisSqlSessionFactoryBean factory = new MybatisSqlSessionFactoryBean();
    factory.setDataSource(dataSource);
    factory.setVfs(SpringBootVFS.class);
    if (StringUtils.hasText(this.properties.getConfigLocation())) {
        factory.setConfigLocation(this.resourceLoader.getResource(this.properties.getConfigLocation()));
    }
    applyConfiguration(factory);
    // ...
    return factory;
}

// TODO 入参使用 MybatisSqlSessionFactoryBean
private void applyConfiguration(MybatisSqlSessionFactoryBean factory) {
    // TODO 使用 MybatisConfiguration
    MybatisConfiguration configuration = this.properties.getConfiguration();
    if (configuration == null && !StringUtils.hasText(this.properties.getConfigLocation())) {
        configuration = new MybatisConfiguration();
    }
    if (configuration != null && !CollectionUtils.isEmpty(this.configurationCustomizers)) {
        for (ConfigurationCustomizer customizer : this.configurationCustomizers) {
            customizer.customize(configuration);
        }
    }
    factory.setConfiguration(configuration);
}
```



> 源码注释是中文。

MybatisConfiguration中比较大的区别是使用了自己的MybatisMapperRegistry作为mapper注册中心。

> MybatisMapperRegistry中有一些属性与Configuration一样，疑似因为早期Configuration内部属性都是私有的，子类无法使用，所以新定义了，导致使用这些属性的方法也需要重写。

来看看MybatisMapperRegistry的核心方法addMapper()

```java
@Override
public <T> void addMapper(Class<T> type) {
    if (type.isInterface()) {
        if (hasMapper(type)) {
            // TODO 如果之前注入 直接返回
            return;
            // TODO 这里就不抛异常了
            //                throw new BindingException("Type " + type + " is already known to the MapperRegistry.");
        }
        boolean loadCompleted = false;
        try {
            // TODO 这里也换成 MybatisMapperProxyFactory 而不是 MapperProxyFactory
            knownMappers.put(type, new MybatisMapperProxyFactory<>(type));
            // It's important that the type is added before the parser is run
            // otherwise the binding may automatically be attempted by the
            // mapper parser. If the type is already known, it won't try.
            // TODO 这里也换成 MybatisMapperAnnotationBuilder 而不是 MapperAnnotationBuilder
            MybatisMapperAnnotationBuilder parser = new MybatisMapperAnnotationBuilder(config, type);
            parser.parse();
            loadCompleted = true;
        } finally {
            if (!loadCompleted) {
                knownMappers.remove(type);
            }
        }
    }
}
```

对照发现主要区别还是一个自定义的MybatisMapperAnnotationBuilder解析器。

MybatisMapperAnnotationBuilder继承自MapperAnnotationBuilder。

> 虽然是继承，但父类MapperAnnotationBuilder中的属性都是私有，子类方法无法直接访问。导致MybatisMapperAnnotationBuilder无法在自己的方法中使用父类属性，只能再声明一遍，再重写大量方法来保证访问的是子类自己的属性，即使只是把原方法cv一遍。
>
> 想来Mybatis作者也没想到有人会对这个进行扩展。

```java
// MybatisMapperAnnotationBuilder.class
@Override
public void parse() {
    String resource = type.toString();
    if (!configuration.isResourceLoaded(resource)) {
        loadXmlResource();
        configuration.addLoadedResource(resource);
        String mapperName = type.getName();
        assistant.setCurrentNamespace(mapperName);
        parseCache();
        parseCacheRef();
        InterceptorIgnoreHelper.InterceptorIgnoreCache cache = InterceptorIgnoreHelper.initSqlParserInfoCache(type);
        for (Method method : type.getMethods()) {
            if (!canHaveStatement(method)) {
                continue;
            }
            if (getAnnotationWrapper(method, false, Select.class, SelectProvider.class).isPresent()
                && method.getAnnotation(ResultMap.class) == null) {
                parseResultMap(method);
            }
            try {
                // TODO 加入 注解过滤缓存
                InterceptorIgnoreHelper.initSqlParserInfoCache(cache, mapperName, method);
                parseStatement(method);
            } catch (IncompleteElementException e) {
                // TODO 使用 MybatisMethodResolver 而不是 MethodResolver
                configuration.addIncompleteMethod(new MybatisMethodResolver(this, method));
            }
        }
        // TODO 注入 CURD 动态 SQL , 放在在最后, because 可能会有人会用注解重写sql
        try {
            // https://github.com/baomidou/mybatis-plus/issues/3038
            if (GlobalConfigUtils.isSupperMapperChildren(configuration, type)) {
                // mapper接口是否继承了Mapper.class类
                parserInjector();
            }
        } catch (IncompleteElementException e) {
            configuration.addIncompleteMethod(new InjectorResolver(this));
        }
    }
    parsePendingMethods();
}

void parserInjector() {
    GlobalConfigUtils.getSqlInjector(configuration).inspectInject(assistant, type);
}
```

主要区别在于最后的注入动态SQL，即方法parserInjector()。

```java
// GlobalConfigUtils.class
/**
 * 缓存全局信息
 */
private static final Map<String, GlobalConfig> GLOBAL_CONFIG = new ConcurrentHashMap<>();

public static ISqlInjector getSqlInjector(Configuration configuration) {
    return getGlobalConfig(configuration).getSqlInjector();
}

public static GlobalConfig getGlobalConfig(Configuration configuration) {
    Assert.notNull(configuration, "Error: You need Initialize MybatisConfiguration !");
    final String key = Integer.toHexString(configuration.hashCode());
    return CollectionUtils.computeIfAbsent(GLOBAL_CONFIG, key, k -> defaults());
}
```

暂时不清楚GLOBAL_CONFIG这个Map有什么用。

> TODO

直接来看看ISqlInjector接口。

```java
/**
 * SQL 自动注入器接口
 *
 * @author hubin
 * @since 2016-07-24
 */
public interface ISqlInjector {

    /**
     * 检查SQL是否注入(已经注入过不再注入)
     *
     * @param builderAssistant mapper 信息
     * @param mapperClass      mapper 接口的 class 对象
     */
    void inspectInject(MapperBuilderAssistant builderAssistant, Class<?> mapperClass);
}
```

再看看ISqlInjector接口的mybatis-plus官方唯一默认实现DefaultSqlInjector。ISqlInjector接口实现在DefaultSqlInjector父类AbstractSqlInjector中。

```java
// AbstractSqlInjector.class
@Override
public void inspectInject(MapperBuilderAssistant builderAssistant, Class<?> mapperClass) {
    // 获取Mapper接口定义的泛型
    Class<?> modelClass = ReflectionKit.getSuperClassGenericType(mapperClass, Mapper.class, 0);
    if (modelClass != null) {
        String className = mapperClass.toString();
        Set<String> mapperRegistryCache = GlobalConfigUtils.getMapperRegistryCache(builderAssistant.getConfiguration());
        if (!mapperRegistryCache.contains(className)) {
            TableInfo tableInfo = TableInfoHelper.initTableInfo(builderAssistant, modelClass);
            List<AbstractMethod> methodList = this.getMethodList(mapperClass, tableInfo);
            if (CollectionUtils.isNotEmpty(methodList)) {
                // 循环注入自定义方法
                methodList.forEach(m -> m.inject(builderAssistant, mapperClass, modelClass, tableInfo));
            } else {
                logger.debug(mapperClass.toString() + ", No effective injection method was found.");
            }
            mapperRegistryCache.add(className);
        }
    }
}

public abstract List<AbstractMethod> getMethodList(Class<?> mapperClass,TableInfo tableInfo);
```

getMethodList()方法实现在DefaultSqlInjector中

```java
public class DefaultSqlInjector extends AbstractSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
        Stream.Builder<AbstractMethod> builder = Stream.<AbstractMethod>builder()
            .add(new Insert())
            .add(new Delete())
            .add(new DeleteByMap())
            .add(new Update())
            .add(new SelectByMap())
            .add(new SelectCount())
            .add(new SelectMaps())
            .add(new SelectMapsPage())
            .add(new SelectObjs())
            .add(new SelectList())
            .add(new SelectPage());
        if (tableInfo.havePK()) {
            builder.add(new DeleteById())
                .add(new DeleteBatchByIds())
                .add(new UpdateById())
                .add(new SelectById())
                .add(new SelectBatchByIds());
        } else {
            logger.warn(String.format("%s ,Not found @TableId annotation, Cannot use Mybatis-Plus 'xxById' Method.",
                tableInfo.getEntityType()));
        }
        return builder.build().collect(toList());
    }
}
```

可以看到里面都是mybatis-plus接口BaseMapper的方法

AbstractMethod

```java
// AbstractMethod.class
public void inject(MapperBuilderAssistant builderAssistant, Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
    this.configuration = builderAssistant.getConfiguration();
    this.builderAssistant = builderAssistant;
    this.languageDriver = configuration.getDefaultScriptingLanguageInstance();
    /* 注入自定义方法 */
    injectMappedStatement(mapperClass, modelClass, tableInfo);
}

public abstract MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo);
```

拿SelectList看下代码

```java
// SelectList.class
@Override
public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
    SqlMethod sqlMethod = SqlMethod.SELECT_LIST;
    String sql = String.format(sqlMethod.getSql(), sqlFirst(), sqlSelectColumns(tableInfo, true), tableInfo.getTableName(),
        sqlWhereEntityWrapper(true, tableInfo), sqlOrderBy(tableInfo), sqlComment());
    SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
    return this.addSelectMappedStatementForTable(mapperClass, getMethod(sqlMethod), sqlSource, tableInfo);
}
```

主要功能是替代xml生成mybatis的SqlSource脚本。

> 无非是根据entity生成的TableInfo信息解析，用上注解反射等，有兴趣再看。

最后addSelectMappedStatementForTable()加到config的mappedStatement中。

```java
// AbstractMethod.classs
protected MappedStatement addSelectMappedStatementForTable(Class<?> mapperClass, String id, SqlSource sqlSource,
                                                           TableInfo table) {
    String resultMap = table.getResultMap();
    if (null != resultMap) {
        /* 返回 resultMap 映射结果集 */
        return addMappedStatement(mapperClass, id, sqlSource, SqlCommandType.SELECT, null,
            resultMap, null, NoKeyGenerator.INSTANCE, null, null);
    } else {
        /* 普通查询 */
        return addSelectMappedStatementForOther(mapperClass, id, sqlSource, table.getEntityType());
    }
}
```
两个分支最终调用的都是addMappedStatement()方法，区别在于resultMap和resultType的入参
```java
protected MappedStatement addSelectMappedStatementForOther(Class<?> mapperClass, String id, SqlSource sqlSource,
                                                           Class<?> resultType) {
    return addMappedStatement(mapperClass, id, sqlSource, SqlCommandType.SELECT, null,
                              null, resultType, NoKeyGenerator.INSTANCE, null, null);
}

protected MappedStatement addMappedStatement(Class<?> mapperClass, String id, SqlSource sqlSource,
                                             SqlCommandType sqlCommandType, Class<?> parameterType,
                                             String resultMap, Class<?> resultType, KeyGenerator keyGenerator,
                                             String keyProperty, String keyColumn) {
    String statementName = mapperClass.getName() + DOT + id;
    if (hasMappedStatement(statementName)) {
        logger.warn(LEFT_SQ_BRACKET + statementName + "] Has been loaded by XML or SqlProvider or Mybatis's Annotation, so ignoring this injection for [" + getClass() + RIGHT_SQ_BRACKET);
        return null;
    }
    /* 缓存逻辑处理 */
    boolean isSelect = sqlCommandType == SqlCommandType.SELECT;
    return builderAssistant.addMappedStatement(id, sqlSource, StatementType.PREPARED, sqlCommandType,
                                               null, null, null, parameterType, resultMap, resultType,
                                               null, !isSelect, isSelect, false, keyGenerator, keyProperty, keyColumn,
                                               configuration.getDatabaseId(), languageDriver, null);
}
```

builderAssistant就是mybatis原版类MapperBuilderAssistant了。plus的Mapper流程至此结束。

# sql流程

获取到的Mapper接口实际上被包装成为了代理对象，所以我们执行查询语句肯定是执行的代理对象方法，接下来我们就以Mapper接口的代理对象MapperProxy来分析一下查询流程。

![MapperInvoke](picture\InvokeMapper.webp)

MapperProxy代理方法

```java
// MapperProxy.class
@Override
public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    try {
        if (Object.class.equals(method.getDeclaringClass())) {
            // 跳过Object的原生方法
            return method.invoke(this, args);
        } else {
            // 构造并缓存
            return cachedInvoker(method).invoke(proxy, method, args, sqlSession);
        }
    } catch (Throwable t) {
        throw ExceptionUtil.unwrapThrowable(t);
    }
}

private final Map<Method, MapperMethodInvoker> methodCache;

private static final Method privateLookupInMethod;

private MapperMethodInvoker cachedInvoker(Method method) throws Throwable {
    try {
        // map缓存及cas
        return MapUtil.computeIfAbsent(methodCache, method, m -> {
            if (m.isDefault()) {
            	// jdk8开始的接口default方法例外，特殊处理
                try {
                    if (privateLookupInMethod == null) {
                        return new DefaultMethodInvoker(getMethodHandleJava8(method));
                    } else {
                        return new DefaultMethodInvoker(getMethodHandleJava9(method));
                    }
                } catch (IllegalAccessException | InstantiationException | InvocationTargetException
                         | NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            } else {
                // 常规接口
                return new PlainMethodInvoker(new MapperMethod(mapperInterface, method, sqlSession.getConfiguration()));
            }
        });
    } catch (RuntimeException re) {
        Throwable cause = re.getCause();
        throw cause == null ? re : cause;
    }
}
```

先看常规的接口MapperMethod构造，其实就是封装。后文执行到时，再进行解析。

```java
public class MapperMethod {

    private final SqlCommand command;
    private final MethodSignature method;

    public MapperMethod(Class<?> mapperInterface, Method method, Configuration config) {
        this.command = new SqlCommand(config, mapperInterface, method);
        this.method = new MethodSignature(config, mapperInterface, method);
    }
}
```

PlainMethodInvoker是内部类，也是一层封装，先跳过。

```java
// MapperProxy.class
private static class PlainMethodInvoker implements MapperMethodInvoker {
    private final MapperMethod mapperMethod;

    public PlainMethodInvoker(MapperMethod mapperMethod) {
        super();
        this.mapperMethod = mapperMethod;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args, SqlSession sqlSession) throws Throwable {
        return mapperMethod.execute(sqlSession, args);
    }
}
```

# sql执行

cachedInvoker获取MapperMethodInvoker后invoke执行。经过PlainMethodInvoker后到达MapperMethod.execute()。

![InvokeSql](picture\InvokeSql.webp)

```java
// MapperMethod.class
public Object execute(SqlSession sqlSession, Object[] args) {
    Object result;
    switch (command.getType()) {
        case INSERT: {
            Object param = method.convertArgsToSqlCommandParam(args);
            result = rowCountResult(sqlSession.insert(command.getName(), param));
            break;
        }
        case UPDATE: {
            Object param = method.convertArgsToSqlCommandParam(args);
            result = rowCountResult(sqlSession.update(command.getName(), param));
            break;
        }
        case DELETE: {
            Object param = method.convertArgsToSqlCommandParam(args);
            result = rowCountResult(sqlSession.delete(command.getName(), param));
            break;
        }
        case SELECT:
            if (method.returnsVoid() && method.hasResultHandler()) {
                executeWithResultHandler(sqlSession, args);
                result = null;
            } else if (method.returnsMany()) {
                result = executeForMany(sqlSession, args);
            } else if (method.returnsMap()) {
                result = executeForMap(sqlSession, args);
            } else if (method.returnsCursor()) {
                result = executeForCursor(sqlSession, args);
            } else {
                Object param = method.convertArgsToSqlCommandParam(args);
                result = sqlSession.selectOne(command.getName(), param);
                if (method.returnsOptional()
                    && (result == null || !method.getReturnType().equals(result.getClass()))) {
                    result = Optional.ofNullable(result);
                }
            }
            break;
        case FLUSH:
            result = sqlSession.flushStatements();
            break;
        default:
            throw new BindingException("Unknown execution method for: " + command.getName());
    }
    if (result == null && method.getReturnType().isPrimitive() && !method.returnsVoid()) {
        throw new BindingException("Mapper method '" + command.getName()
                                   + " attempted to return null from a method with a primitive return type (" + method.getReturnType() + ").");
    }
    return result;
}
```

几个核心的方法

rowCountResult()，返回插入、更新、删除的计数。比较简单独立。

```java
private Object rowCountResult(int rowCount) {
    final Object result;
    if (method.returnsVoid()) {
        result = null;
    } else if (Integer.class.equals(method.getReturnType()) || Integer.TYPE.equals(method.getReturnType())) {
        result = rowCount;
    } else if (Long.class.equals(method.getReturnType()) || Long.TYPE.equals(method.getReturnType())) {
        result = (long) rowCount;
    } else if (Boolean.class.equals(method.getReturnType()) || Boolean.TYPE.equals(method.getReturnType())) {
        result = rowCount > 0;
    } else {
        throw new BindingException("Mapper method '" + command.getName() + "' has an unsupported return type: " + method.getReturnType());
    }
    return result;
}
```

convertArgsToSqlCommandParam()，解析接口参数，并映射成脚本占位符。

```java
// MapperMethod.MethodSignature.class
public Object convertArgsToSqlCommandParam(Object[] args) {
    return paramNameResolver.getNamedParams(args);
}
```

```java
// ParamNameResolver.class
public Object getNamedParams(Object[] args) {
    final int paramCount = names.size();
    if (args == null || paramCount == 0) {
        return null;
    } else if (!hasParamAnnotation && paramCount == 1) {
        Object value = args[names.firstKey()];
        return wrapToMapIfCollection(value, useActualParamName ? names.get(0) : null);
    } else {
        final Map<String, Object> param = new ParamMap<>();
        int i = 0;
        for (Map.Entry<Integer, String> entry : names.entrySet()) {
            param.put(entry.getValue(), args[entry.getKey()]);
            // add generic param names (param1, param2, ...)
            // 默认名
            final String genericParamName = GENERIC_NAME_PREFIX + (i + 1);
            // ensure not to overwrite parameter named with @Param
            if (!names.containsValue(genericParamName)) {
                // @Param注解名
                param.put(genericParamName, args[entry.getKey()]);
            }
            i++;
        }
        return param;
    }
}
```

解析完就是正戏了，已update为例

sqlSession.update()

```java
// SqlSessionTemplate.class
@Override
public int update(String statement, Object parameter) {
    return this.sqlSessionProxy.update(statement, parameter);
}
```



DefaultSqlSession

```java
// DefaultSqlSession.class
@Override
public int update(String statement, Object parameter) {
    try {
        dirty = true;
        MappedStatement ms = configuration.getMappedStatement(statement);
        return executor.update(ms, wrapCollection(parameter));
    } catch (Exception e) {
        throw ExceptionFactory.wrapException("Error updating database.  Cause: " + e, e);
    } finally {
        ErrorContext.instance().reset();
    }
}
```

> dirty干嘛的

```java
// CachingExecutor.class
@Override
public int update(MappedStatement ms, Object parameterObject) throws SQLException {
    // 应该是缓存机制，CachingExecutor的区别
    flushCacheIfRequired(ms);
    // private final Executor delegate;
    return delegate.update(ms, parameterObject);
}
```
BaseExecutor
```java
// BaseExecutor.class
@Override
public int update(MappedStatement ms, Object parameter) throws SQLException {
    ErrorContext.instance().resource(ms.getResource()).activity("executing an update").object(ms.getId());
    if (closed) {
        throw new ExecutorException("Executor was closed.");
    }
    clearLocalCache();
    return doUpdate(ms, parameter);
}

@Override
public int doUpdate(MappedStatement ms, Object parameter) throws SQLException {
    Statement stmt = null;
    try {
        Configuration configuration = ms.getConfiguration();
        StatementHandler handler = configuration.newStatementHandler(this, ms, parameter, RowBounds.DEFAULT, null, null);
        // 参数替换占位符
        stmt = prepareStatement(handler, ms.getStatementLog());
        return handler.update(stmt);
    } finally {
        closeStatement(stmt);
    }
}

```

RoutingStatementHandler

```java
// RoutingStatementHandler.class
@Override
public int update(Statement statement) throws SQLException {
    // private final StatementHandler delegate;
    return delegate.update(statement);
}
```

PreparedStatementHandler，真正执行的方法ps.execute()

```java
// PreparedStatementHandler.class
@Override
public int update(Statement statement) throws SQLException {
    PreparedStatement ps = (PreparedStatement) statement;
    ps.execute();
    int rows = ps.getUpdateCount();
    Object parameterObject = boundSql.getParameterObject();
    KeyGenerator keyGenerator = mappedStatement.getKeyGenerator();
    keyGenerator.processAfter(executor, mappedStatement, ps, parameterObject);
    return rows;
}
```

# 参数映射

参数映射，替换statement脚本占位符。

回到SimpleExecutor.doUpdate()方法，调用了prepareStatement()。

```java
// SimpleExecutor.class
private Statement prepareStatement(StatementHandler handler, Log statementLog) throws SQLException {
  Statement stmt;
  Connection connection = getConnection(statementLog);
  stmt = handler.prepare(connection, transaction.getTimeout());
  handler.parameterize(stmt);
  return stmt;
}
```

handler.parameterize()

```java
// PreparedStatementHandler.class
@Override
public void parameterize(Statement statement) throws SQLException {
	parameterHandler.setParameters((PreparedStatement) statement);
}
```

parameterHandler.setParameters((PreparedStatement) statement);

```java
// class DefaultParameterHandler implements ParameterHandler
@Override
public void setParameters(PreparedStatement ps) {
    ErrorContext.instance().activity("setting parameters").object(mappedStatement.getParameterMap().getId());
    List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
    if (parameterMappings != null) {
        for (int i = 0; i < parameterMappings.size(); i++) {
            ParameterMapping parameterMapping = parameterMappings.get(i);
            if (parameterMapping.getMode() != ParameterMode.OUT) {
                Object value;
                String propertyName = parameterMapping.getProperty();
                if (boundSql.hasAdditionalParameter(propertyName)) { // issue #448 ask first for additional params
                    // key匹配
                    value = boundSql.getAdditionalParameter(propertyName);
                } else if (parameterObject == null) {
                    // null
                    value = null;
                } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                    // 
                    value = parameterObject;
                } else {
                    // 
                    MetaObject metaObject = configuration.newMetaObject(parameterObject);
                    value = metaObject.getValue(propertyName);
                }
                TypeHandler typeHandler = parameterMapping.getTypeHandler();
                JdbcType jdbcType = parameterMapping.getJdbcType();
                if (value == null && jdbcType == null) {
                    jdbcType = configuration.getJdbcTypeForNull();
                }
                try {
                    // set
                    typeHandler.setParameter(ps, i + 1, value, jdbcType);
                } catch (TypeException | SQLException e) {
                    throw new TypeException("Could not set parameters for mapping: " + parameterMapping + ". Cause: " + e, e);
                }
            }
        }
    }
}
```

对于未指定typeHandler的字段，会返回UnknownTypeHandler，再根据jdbcType和javaType获取对应的typeHandler。

```java
// class UnknownTypeHandler extends BaseTypeHandler<Object>
@Override
public void setParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
    if (parameter == null) {
        if (jdbcType == null) {
            throw new TypeException("JDBC requires that the JdbcType must be specified for all nullable parameters.");
        }
        try {
            ps.setNull(i, jdbcType.TYPE_CODE);
        } catch (SQLException e) {
            throw new TypeException("Error setting null for parameter #" + i + " with JdbcType " + jdbcType + " . "
                                    + "Try setting a different JdbcType for this parameter or a different jdbcTypeForNull configuration property. "
                                    + "Cause: " + e, e);
        }
    } else {
        try {
            setNonNullParameter(ps, i, parameter, jdbcType);
        } catch (Exception e) {
            throw new TypeException("Error setting non null for parameter #" + i + " with JdbcType " + jdbcType + " . "
                                    + "Try setting a different JdbcType for this parameter or a different configuration property. "
                                    + "Cause: " + e, e);
        }
    }
}

@Override
public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType)
    throws SQLException {
    TypeHandler handler = resolveTypeHandler(parameter, jdbcType);
    handler.setParameter(ps, i, parameter, jdbcType);
}

private TypeHandler<?> resolveTypeHandler(Object parameter, JdbcType jdbcType) {
    TypeHandler<?> handler;
    if (parameter == null) {
        handler = OBJECT_TYPE_HANDLER;
    } else {
        // 获取javaType和jdbcType对应的typeHandler
        handler = typeHandlerRegistrySupplier.get().getTypeHandler(parameter.getClass(), jdbcType);
        // check if handler is null (issue #270)
        if (handler == null || handler instanceof UnknownTypeHandler) {
            handler = OBJECT_TYPE_HANDLER;
        }
    }
    return handler;
}
```







spring事务代理，代理SqlSession接口实例，即多次提到的DeafuleSqlSession。

```java
private class SqlSessionInterceptor implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        SqlSession sqlSession = getSqlSession(SqlSessionTemplate.this.sqlSessionFactory,
                                              SqlSessionTemplate.this.executorType, SqlSessionTemplate.this.exceptionTranslator);
        try {
            Object result = method.invoke(sqlSession, args);
            if (!isSqlSessionTransactional(sqlSession, SqlSessionTemplate.this.sqlSessionFactory)) {
                // force commit even on non-dirty sessions because some databases require
                // a commit/rollback before calling close()
                sqlSession.commit(true);
            }
            return result;
        } catch (Throwable t) {
            Throwable unwrapped = unwrapThrowable(t);
            if (SqlSessionTemplate.this.exceptionTranslator != null && unwrapped instanceof PersistenceException) {
                // release the connection to avoid a deadlock if the translator is no loaded. See issue #22
                closeSqlSession(sqlSession, SqlSessionTemplate.this.sqlSessionFactory);
                sqlSession = null;
                Throwable translated = SqlSessionTemplate.this.exceptionTranslator
                    .translateExceptionIfPossible((PersistenceException) unwrapped);
                if (translated != null) {
                    unwrapped = translated;
                }
            }
            throw unwrapped;
        } finally {
            if (sqlSession != null) {
                closeSqlSession(sqlSession, SqlSessionTemplate.this.sqlSessionFactory);
            }
        }
    }
}
```

