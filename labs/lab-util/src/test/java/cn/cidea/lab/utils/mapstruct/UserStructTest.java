package cn.cidea.lab.utils.mapstruct;

import cn.cidea.lab.utils.entity.User;
import cn.cidea.lab.utils.dto.UserDTO;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserStructTest {

    @Test
    public void toDTO(){
        User user = new User();
        user.setId("1")
                .setName("匿名")
                .setDeptId("D01")
                .setDeleted(false);

        UserStruct struct = UserStruct.INSTANCE;
        UserDTO userDTO;
        userDTO = struct.auto(user);
        userDTO = struct.map(user);
        return;
    }

}