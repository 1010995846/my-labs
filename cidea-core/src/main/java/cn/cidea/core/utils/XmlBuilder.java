package cn.cidea.core.utils;


import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Optional;

/**
 * @author Charlotte
 */
@Slf4j
public class XmlBuilder {

    private String rootName;

    private Object obj;

    /**
     * 是否节点内部属性
     */
    private boolean inner;

    /**
     * 是否填充空字符
     */
    private boolean fillEmpty;

    public XmlBuilder(String rootName, Object obj) {
        if (StringUtils.isBlank(rootName)) {
            throw new IllegalArgumentException("xml rootName must not be null!");
        }
        if (obj == null) {
            throw new IllegalArgumentException("xml obj must not be null!");
        }
        if (ClassUtils.isPrimitiveOrWrapper(obj.getClass())) {
            throw new IllegalArgumentException("xml obj must not be primitive or wrapper!");
        }
        this.obj = obj;
        this.rootName = rootName;
    }

    public XmlBuilder inner() {
        this.inner = true;
        return this;
    }

    public XmlBuilder fillEmpty() {
        this.fillEmpty = true;
        return this;
    }

    public StringBuilder builder() {
        StringBuilder parent = new StringBuilder();
        builder(parent, rootName, obj, obj.getClass());
        return parent;
    }

    private StringBuilder builder(StringBuilder parent, String nodeName, Object node, Class nodeClass) {
        if (isBasicOrString(nodeClass)) {
            // TODO field注解配置
            StringBuilder builder = new StringBuilder();
            boolean inner = this.inner;
            String value = Optional.ofNullable(node).map(Object::toString).orElse(fillEmpty ? "" : null);
            if (inner) {
                // 父节点内部属性
                parent.append(nodeName).append("=\"").append(value).append("\" ");
            } else {
                // 子节点
                builder.append("<").append(nodeName).append(">").append(value).append("</").append(nodeName).append(">\n");
            }
            return builder;
        }
        if (node == null) {
            if(fillEmpty){
                try {
                    node = nodeClass.newInstance();
                } catch (Exception e) {
                    log.error("xml build, get field value fail", e);
                }
            }
            if(node == null){
                return new StringBuilder();
            }
        }
        if (isIterable(nodeClass)) {
            StringBuilder builder = new StringBuilder();
            Iterator<?> iterator = ((Iterable<?>) obj).iterator();
            while (iterator.hasNext()) {
                Object next = iterator.next();
                if (next == null) {
                    continue;
                }
                builder.append(builder(parent, nodeName, next, next.getClass()));
            }
            return builder;
        } else {
            parent = new StringBuilder();
            parent.append("<").append(nodeName).append(" ");
            StringBuilder builder = new StringBuilder();
            for (Field field : nodeClass.getDeclaredFields()) {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object fieldValue = null;
                Class<?> fieldType = field.getType();
                if (node != null) {
                    try {
                        fieldValue = field.get(node);
                    } catch (IllegalAccessException e) {
                        log.error("xml build, get field value fail", e);
                        continue;
                    }
                }
                builder.append(builder(parent, fieldName, fieldValue, fieldType));
            }
            if(builder.length() == 0){
                parent.append("/>");
            } else {
                parent.append(">").append(builder).append("</").append(nodeName).append(">\n");
            }
            return parent;
        }
    }

    private boolean isIterable(Class<?> fieldType) {
        return ClassUtils.isAssignable(fieldType, Iterable.class);
    }

    private boolean isBasicOrString(Class<?> fieldType) {
        return ClassUtils.isPrimitiveOrWrapper(fieldType) || ClassUtils.isAssignable(fieldType, CharSequence.class);
    }
}
