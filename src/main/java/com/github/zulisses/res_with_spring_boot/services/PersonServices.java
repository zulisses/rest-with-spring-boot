package com.github.zulisses.res_with_spring_boot.services;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.zulisses.res_with_spring_boot.controllers.PersonController;
import com.github.zulisses.res_with_spring_boot.data.dto.v1.PersonDTO;
import com.github.zulisses.res_with_spring_boot.exception.ResourceNotFoundException;
import static com.github.zulisses.res_with_spring_boot.mapper.ObjectMapper.parseObject;
import static com.github.zulisses.res_with_spring_boot.mapper.ObjectMapper.parseListObjects;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import com.github.zulisses.res_with_spring_boot.model.Person;
import com.github.zulisses.res_with_spring_boot.repository.PersonRepository;

@Service
public class PersonServices {

    private Logger logger = LoggerFactory.getLogger(PersonServices.class);

    @Autowired
    private PersonRepository repository;

    public PersonDTO findById(Long id) {
        logger.info("Finding one person!");
        Person entity = repository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new ResourceNotFoundException("Person not found"));
        var dto = parseObject(entity, PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public List<PersonDTO> findAll() {
        logger.info("Finding all persons!");
        var dtos = parseListObjects(repository.findAll(), PersonDTO.class);
        dtos.forEach(this::addHateoasLinks);
        return dtos;  
    }

    public PersonDTO create(PersonDTO person) {
        logger.info("Creating one person!");

        Person entity = Objects.requireNonNull(parseObject(person, Person.class));
        var dto = parseObject(repository.save(entity), PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public PersonDTO update(PersonDTO person) {
        logger.info("Updating one person!");
        Person entity = repository.findById(Objects.requireNonNull(person.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Person not found"));
        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        var dto = parseObject(repository.save(entity), PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public void delete(Long id) {
        logger.info("Deleting one person!");
        Person person = repository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new ResourceNotFoundException("Person not found"));
        repository.delete(Objects.requireNonNull(person));
    }

    private void addHateoasLinks(PersonDTO dto) {
        dto.add(linkTo(Objects.requireNonNull(methodOn(PersonController.class).findById(dto.getId()))).withSelfRel().withType("GET"));
        dto.add(linkTo(Objects.requireNonNull(methodOn(PersonController.class).findAll())).withRel("findAll").withType("GET"));
        dto.add(linkTo(Objects.requireNonNull(methodOn(PersonController.class).create(dto))).withRel("create").withType("POST"));
        dto.add(linkTo(Objects.requireNonNull(methodOn(PersonController.class).update(dto))).withRel("update").withType("PUT"));
        dto.add(linkTo(Objects.requireNonNull(methodOn(PersonController.class).delete(dto.getId()))).withRel("delete").withType("DELETE"));
    }
}
