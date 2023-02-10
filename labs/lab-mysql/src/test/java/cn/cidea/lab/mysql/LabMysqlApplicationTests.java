package cn.cidea.lab.mysql;

import cn.cidea.module.admin.dataobject.dto.EducationalBackgroundDTO;
import cn.cidea.module.admin.dataobject.entity.Person;
import cn.cidea.module.admin.service.system.IPersonService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
class LabMysqlApplicationTests {

    @Autowired
    private IPersonService personBO;

    @Test
    void contextLoads() {
        List<Person> personList = personBO.list();
        Person person;
        boolean insert = personList.size() == 0;
        if (insert) {
            person = new Person();
        } else {
            person = personList.get(0);
        }
        EducationalBackgroundDTO educationalBackground = person.getEducationalBackground();
        if (educationalBackground == null) {
            educationalBackground = new EducationalBackgroundDTO();
            person.setEducationalBackground(educationalBackground);
        }
        educationalBackground.setUniversity("华中");
        if(insert){
            personBO.save(person);
        } else {
            personBO.updateById(person);
        }
        return;
    }

}
