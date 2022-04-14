package cn.cidea.lab.utils.mapstruct;

import cn.cidea.lab.utils.dto.UserDTO;
import cn.cidea.lab.utils.entity.Dept;
import cn.cidea.lab.utils.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @author Charlotte
 */
@Mapper(componentModel = "spring")
public interface UserStruct {

    UserStruct INSTANCE = Mappers.getMapper(UserStruct.class);

    /**
     * 对于同名同属性的字段，无需特别声明指定，自动转换
     * 基础数据类型会进行自动隐式的转换，boolean -> String
     * @param user
     * @return
     */
    UserDTO auto(User user);

    /**
     * 可覆盖
     * @param user
     * @return
     */
    @Mapping(source = "name", target = "id")
    UserDTO map(User user);

    /**
     * 支持多映射
     * 但要注意字段重名，编译有提示
     * @param user
     * @param dept
     * @return
     */
    @Mappings(value = {
        @Mapping(source = "user.id", target = "id"),
        @Mapping(source = "user.name", target = "name"),
        @Mapping(source = "user.deleted", target = "deleted"),
        @Mapping(source = "dept.name", target = "deptName"),
    })
    UserDTO mapMany(User user, Dept dept);

    /**
     * field不存在时时会编译错误
     * 字段重命名不会更新索引，需要手动更新
     */
//    @Mapping(source = "dasda", target = "id")
//    UserDTO mapError(User user);


}
