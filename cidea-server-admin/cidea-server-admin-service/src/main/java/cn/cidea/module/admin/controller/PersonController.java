package cn.cidea.module.admin.controller;


import cn.cidea.framework.web.core.api.Response;
import cn.cidea.module.admin.dataobject.convert.PersonConvert;
import cn.cidea.module.admin.dataobject.dto.PersonDTO;
import cn.cidea.module.admin.dataobject.dto.PersonSaveDTO;
import cn.cidea.module.admin.dataobject.entity.Person;
import cn.cidea.module.admin.service.IPersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Charlotte
 */
@Slf4j
@RestController
@RequestMapping(value = "/person")
public class PersonController {
    @Autowired
    private IPersonService personService;

    @RequestMapping(value = "/getOne")
    public Response getOne(@RequestBody PersonDTO dto){
        return Response.success(PersonConvert.INSTANCE.toDTO(personService.getById(dto.getId())));
    }

    @RequestMapping(value = "/save")
    @ResponseBody
    public Response save(@RequestBody PersonSaveDTO dto){
        Person user = personService.save(dto);
        return Response.success(PersonConvert.INSTANCE.toDTO(user));
    }

}
