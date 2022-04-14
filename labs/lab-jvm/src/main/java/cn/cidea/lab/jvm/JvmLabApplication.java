package cn.cidea.lab.jvm;

import cn.cidea.lab.jvm.memory.error.RuntimeConstantPoolOOM;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author Charlotte
 */
@SpringBootApplication
public class JvmLabApplication {

    public static void main(String[] args) throws Throwable {
        ConfigurableApplicationContext context = SpringApplication.run(JvmLabApplication.class, args);

//        DirectMemoryOOM.main(args);
//        HeapOOM.main(args);
//        JavaMethodAreaOOM.main(args);
//        JavaVMStackOOM.main(args);
//        JavaVMStackSOF.main(args);
        RuntimeConstantPoolOOM.main(args);

        return;
    }

}
