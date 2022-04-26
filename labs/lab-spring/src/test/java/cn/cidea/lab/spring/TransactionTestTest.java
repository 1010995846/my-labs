package cn.cidea.lab.spring;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import cn.cidea.server.ServerApplication;
import cn.cidea.server.dataobject.entity.SysUser;
import cn.cidea.server.dal.mysql.ISysUserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

/**
 * 手动声明事务时注意提交/回滚触发结束处理，否则无法释放数据库连接，setRollbackOnly只是标识
 */
@SpringBootTest(classes = ServerApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
class TransactionTestTest {

    @Autowired
    private ISysUserMapper userMapper;

    private String targetName = "回滚";

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Test
    public void rollback() {
        userMapper.delete(new UpdateWrapper<SysUser>().lambda().eq(SysUser::getUsername, targetName));

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus ts = transactionManager.getTransaction(def);

        List<SysUser> list;
        try {

            userMapper.insert(getTargetUser());
            list = getTargetList();
            Assertions.assertTrue(list.size() == 1, "未提交!");
            ts.flush();

            def = new DefaultTransactionDefinition();
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_NOT_SUPPORTED);
            TransactionStatus tsInner = transactionManager.getTransaction(def);
            // 第一个事物提交前，查不到
            list = getTargetList();
            Assertions.assertTrue(list.size() == 0, "未隔离!");
            transactionManager.commit(tsInner);

            // 抛异常触发手动回滚
            if(1 == 1){
                throw new RuntimeException();
            }
        } catch (Throwable t) {

            // 回滚方式一
            // transactionManager.rollback(ts);
//         回滚方式二
            ts.setRollbackOnly();
        } finally {
            transactionManager.commit(ts);
        }

        list = getTargetList();
        Assertions.assertTrue(list.size() == 0, "未回滚!");
        return;
    }

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Test
    public void commitSimple() {
        // 默认与Spring事物一致，表达式内相当于@Translational注解切面
        transactionTemplate.execute(status -> {
            userMapper.delete(new UpdateWrapper<SysUser>().lambda().eq(SysUser::getUsername, targetName));
            userMapper.insert(getTargetUser());
            return null;
        });
        transactionTemplate.execute(status -> {
            List<SysUser> list = getTargetList();
            Assertions.assertTrue(list.size() == 1, "未提交!");
            return null;
        });
    }

    @Test
    public void rollbackSimple() {
        try {
            transactionTemplate.execute(status -> {
                userMapper.delete(new UpdateWrapper<SysUser>().lambda().eq(SysUser::getUsername, targetName));
                userMapper.insert(getTargetUser());
                // 方案一
//                status.setRollbackOnly();
                // 方案二
                if (1 == 1) {
                    throw new RuntimeException();
                }
                return null;
            });
        } catch (RuntimeException r) {

        }
        List<SysUser> list = getTargetList();
        Assertions.assertTrue(list.size() == 0, "未回滚!");
    }

    private List<SysUser> getTargetList() {
        return userMapper.selectList(new QueryWrapper<SysUser>().lambda()
                .eq(SysUser::getUsername, targetName));
    }

    private SysUser getTargetUser() {
        return new SysUser().setUsername(targetName);
    }
}