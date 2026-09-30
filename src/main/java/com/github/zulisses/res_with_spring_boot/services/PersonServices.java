package com.github.zulisses.res_with_spring_boot.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.zulisses.res_with_spring_boot.exception.ResourceNotFoundException;
import com.github.zulisses.res_with_spring_boot.model.Person;
import com.github.zulisses.res_with_spring_boot.repository.PersonRepository;

@Service
public class PersonServices {

    private Logger logger = LoggerFactory.getLogger(PersonServices.class);

    @Autowired
    private PersonRepository repository;

    public Person findById(Long id) {
        logger.info("Finding one person!");
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Person not found"));
    }

    public List<Person> findAll() {
        logger.info("Finding all persons!");
        return repository.findAll();
    }

    public Person create(Person person) {
        logger.info("Creating one person!");
        return repository.save(person);
    }

    public Person update(Person person) {
        logger.info("Updating one person!");
        Person entity = repository.findById(person.getId()).orElseThrow(() -> new ResourceNotFoundException("Person not found"));
        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());
        return repository.save(entity);
    }

    public void delete(Long id) {
        logger.info("Deleting one person!");
        Person person = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Person not found"));
        repository.delete(person);
    }
}
