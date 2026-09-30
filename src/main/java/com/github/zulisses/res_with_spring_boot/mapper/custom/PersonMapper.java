package com.github.zulisses.res_with_spring_boot.mapper.custom;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.github.zulisses.res_with_spring_boot.data.dto.v2.PersonDTOV2;
import com.github.zulisses.res_with_spring_boot.model.Person;

@Service
public class PersonMapper {
    
    public PersonDTOV2 convertEntityToDTO(Person person) {
        PersonDTOV2 dto = new PersonDTOV2();
        dto.setId(person.getId());
        dto.setFirstName(person.getFirstName());
        dto.setLastName(person.getLastName());
        dto.setBirthDate(new Date());
        dto.setAddress(person.getAddress());
        dto.setGender(person.getGender());
        return dto;
    }   

    public Person convertDTOToEntity(PersonDTOV2 dto) {
        Person entity = new Person();
        entity.setId(dto.getId());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        // entity.setBirthDate(new Date());
        entity.setAddress(dto.getAddress());
        entity.setGender(dto.getGender());
        return entity;
    }
}