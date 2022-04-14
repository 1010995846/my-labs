package cn.cidea.lab.mybatis;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.cidea.server.ServerApplication;
import cn.cidea.server.dataobject.entity.SysUser;
import cn.cidea.server.dal.mysql.ISysUserMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;

/**
 * @author Charlotte
 */
@SpringBootApplication
@ComponentScan(basePackageClasses = ServerApplication.class)
public class LabMybatisApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(LabMybatisApplication.class, args);
        ISysUserMapper userMapper = context.getBean(ISysUserMapper.class);
        List<SysUser> users = userMapper.selectList(new QueryWrapper<>());
        return;
    }

}
