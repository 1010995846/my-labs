## MybatisAutoConfiguration



### 初始化SqlSessionFactory



### SqlSessionTemplate






### 创建ClassPathMapperScanner并scan()

当缺失MapperFactoryBean时才会启用配置，前文可以了解到，这是scan时配置到mapperBeanDefinition的beanClass。可认为未扫描到mapper时启用。

导入`AutoConfiguredMapperScannerRegistrar`。

```java
// class MybatisAutoConfiguration implements InitializingBean
@Configuration
@Import({MybatisAutoConfiguration.AutoConfiguredMapperScannerRegistrar.class})
@ConditionalOnMissingBean({MapperFactoryBean.class})
public static class MapperScannerRegistrarNotFoundConfiguration implements InitializingBean {
    public MapperScannerRegistrarNotFoundConfiguration() {
    }

    public void afterPropertiesSet() {
        MybatisAutoConfiguration.logger.debug("No {} found.", MapperFactoryBean.class.getName());
    }
}
```

AutoConfiguredMapperScannerRegistrar



```java
// class AutoConfiguredMapperScannerRegistrar implements BeanFactoryAware, ImportBeanDefinitionRegistrar, ResourceLoaderAware
@Override
public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
    if (!AutoConfigurationPackages.has(this.beanFactory)) {
        MybatisAutoConfiguration.logger.debug("Could not determine auto-configuration package, automatic mapper scanning disabled.");
    } else {
        MybatisAutoConfiguration.logger.debug("Searching for mappers annotated with @Mapper");
        List<String> packages = AutoConfigurationPackages.get(this.beanFactory);
        if (MybatisAutoConfiguration.logger.isDebugEnabled()) {
            packages.forEach((pkg) -> {
                MybatisAutoConfiguration.logger.debug("Using auto-configuration base package '{}'", pkg);
            });
        }

        ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);
        if (this.resourceLoader != null) {
            scanner.setResourceLoader(this.resourceLoader);
        }

		// 扫描指定的Mapper注解
        scanner.setAnnotationClass(Mapper.class);
        scanner.registerFilters();
        // 这里是doScan，和MapperScannerConfigurer中的不一样，暂时不清楚scan多做了什么
        scanner.doScan(StringUtils.toStringArray(packages));
    }
}
```
自动扫描仅扫描@Mapper注解标记的接口，或者使用@MapperScan自定义扫描


# PLUS

MybatisPlusAutoConfiguration

