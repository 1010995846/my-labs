package cn.cidea.lab.mybatis;

import cn.cidea.module.admin.AdminApplication;
import cn.cidea.module.admin.dal.mysql.ISysUserMapper;
import cn.cidea.module.admin.dataobject.entity.SysUser;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;

/**
 * @author Charlotte
 */
@SpringBootApplication
@ComponentScan(basePackageClasses = AdminApplication.class)
public class LabMybatisApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(LabMybatisApplication.class, args);
        ISysUserMapper userMapper = context.getBean(ISysUserMapper.class);
        List<SysUser> users = userMapper.selectList(new QueryWrapper<>());
        return;
    }

}
