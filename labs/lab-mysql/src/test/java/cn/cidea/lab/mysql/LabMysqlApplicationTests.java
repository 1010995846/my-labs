package cn.cidea.lab.mysql;

import cn.cidea.server.service.system.IPersonService;
import cn.cidea.server.dataobject.entity.Person;
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
        Person.EducationalBackground educationalBackground = person.getEducationalBackground();
        if (educationalBackground == null) {
            educationalBackground = new Person.EducationalBackground();
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
