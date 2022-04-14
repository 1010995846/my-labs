package cn.cidea.lab.jvm.clazz;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

public class ClassLoaderTest {

    @Test
    public void equlas() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        ClassLoader myLoader = new ClassLoader() {
            @Override
            public Class<?> loadClass(String name) throws ClassNotFoundException {
                try {
                    String fileName = name.substring(name.lastIndexOf(".") + 1) + ".class";
                    InputStream is = getClass().getResourceAsStream(fileName);
                    if (is == null) {
                        return super.loadClass(name);
                    }
                    byte[] b = new byte[is.available()];
                    is.read(b);
                    return defineClass(name, b, 0, b.length);
                } catch (IOException e) {
                    throw new ClassNotFoundException(name);
                }
            }
        };

        Object obj = myLoader.loadClass(ClassLoaderTest.class.getName()).newInstance();
        System.out.println(obj.getClass());
        System.out.println(obj instanceof ClassLoaderTest);
        System.out.println(obj.getClass().equals(ClassLoaderTest.class));
        // obj实例使用了自定义的MyClassLoader加载，而默认的ClassLoaderTest类文件加载时使用的是默认的ClassLoader。
        // 类加载器不一致，即使加载的二进制字节流相同，但在jvm中也视为独立的类，即false
    }

}