package com.github.zulisses.res_with_spring_boot.services;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.github.zulisses.res_with_spring_boot.controllers.PersonController;
import com.github.zulisses.res_with_spring_boot.data.dto.v1.PersonDTO;
import com.github.zulisses.res_with_spring_boot.exception.BadRequestException;
import com.github.zulisses.res_with_spring_boot.exception.FileStorageException;
import com.github.zulisses.res_with_spring_boot.exception.RequiredObjectIsNullException;
import com.github.zulisses.res_with_spring_boot.exception.ResourceNotFoundException;
import com.github.zulisses.res_with_spring_boot.file.importer.contract.FileImporter;
import com.github.zulisses.res_with_spring_boot.file.importer.factory.FileImporterFactory;

import static com.github.zulisses.res_with_spring_boot.mapper.ObjectMapper.parseObject;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import com.github.zulisses.res_with_spring_boot.model.Person;
import com.github.zulisses.res_with_spring_boot.repository.PersonRepository;

import jakarta.transaction.Transactional;

@Service
public class PersonServices {

    private Logger logger = LoggerFactory.getLogger(PersonServices.class);

    @Autowired
    private PersonRepository repository;

    @Autowired 
    FileImporterFactory importer;

    @Autowired
    PagedResourcesAssembler<PersonDTO> assembler;

    public PersonDTO findById(Long id) {
        logger.info("Finding one person!");
        Person entity = repository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new ResourceNotFoundException("Person not found"));
        var dto = parseObject(entity, PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public PagedModel<EntityModel<PersonDTO>> findAll(@NonNull Pageable pageable) {
        logger.info("Finding all persons!");

        Page<Person> people = repository.findAll(pageable);

        return buildPageModel(pageable, people);
    }

    public PagedModel<EntityModel<PersonDTO>> findPeopleByFirstName(String firstName, @NonNull Pageable pageable) {
        logger.info("Finding all persons by first name!");

        Page<Person> people = repository.findPeopleByFirstName(firstName, pageable);

        return buildPageModel(pageable, people);
    }

    public PersonDTO create(PersonDTO person) {
        if (person == null) {
            throw new RequiredObjectIsNullException();
        }
        logger.info("Creating one person!");

        Person entity = Objects.requireNonNull(parseObject(person, Person.class));
        var dto = parseObject(repository.save(entity), PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public List<PersonDTO> massCreation(MultipartFile file) {
        logger.info("Importing people from file!");

        if (file.isEmpty()) throw new BadRequestException("Please set a valid file!");

        try(InputStream inputStream = file.getInputStream()) {
            String filename = Optional.ofNullable(file.getOriginalFilename())
                .orElseThrow(() -> new BadRequestException("File name cannot be null!"));
            logger.info("Filename: {}", filename);
            FileImporter importer = this.importer.getImporter(filename);
            logger.info("Importer: {}", importer);
            List<Person> entities = importer.importFile(inputStream).stream()
                .map(dto -> repository.save(parseObject(dto, Person.class)))
                .toList();
            logger.info("Entities: {}", entities);
            return entities.stream().map(person -> {
                logger.info("Person: {}", person);
                PersonDTO dto = parseObject(person, PersonDTO.class);
                addHateoasLinks(dto);
                return dto;
            }).toList();
        } catch (Exception e) {
            throw new FileStorageException("Error processing the file!");
        }
    }

    public PersonDTO update(PersonDTO person) {
        if (person == null) {
            throw new RequiredObjectIsNullException();
        }
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

    @Transactional
    public PersonDTO disablePerson(Long id) {
        logger.info("Disabling one person!");

        repository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new ResourceNotFoundException("Person not found"));

        repository.disablePerson(Objects.requireNonNull(id));

        var entity = repository.findById(Objects.requireNonNull(id)).get();
        var dto = parseObject(entity, PersonDTO.class);
        addHateoasLinks(dto);

        return dto;
    }

    private PagedModel<EntityModel<PersonDTO>> buildPageModel(Pageable pageable, Page<Person> people) {
        Page<PersonDTO> peopleWithLinks = people.map(person -> {
            PersonDTO dto = parseObject(person, PersonDTO.class);
            addHateoasLinks(dto);
            return dto;
        });

        Link findAllLink = WebMvcLinkBuilder.linkTo(Objects.requireNonNull(
                WebMvcLinkBuilder.methodOn(PersonController.class)
                        .findAll(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort().toString())))
                .withSelfRel();

        return assembler.toModel(peopleWithLinks, findAllLink);
    }

    private void addHateoasLinks(PersonDTO dto) {
        dto.add(linkTo(Objects.requireNonNull(methodOn(PersonController.class).findById(dto.getId()))).withSelfRel()
                .withType("GET"));
        dto.add(linkTo(Objects.requireNonNull(methodOn(PersonController.class).findAll(0, 12, "asc")))
                .withRel("findAll").withType("GET"));
        dto.add(linkTo(Objects.requireNonNull(
                methodOn(PersonController.class).findPeopleByFirstName(dto.getFirstName(), 0, 12, "asc")))
                .withRel("findPeopleByFirstName").withType("GET"));
        dto.add(linkTo(Objects.requireNonNull(methodOn(PersonController.class).create(dto))).withRel("create")
                .withType("POST"));
        dto.add(linkTo(Objects.requireNonNull(methodOn(PersonController.class))).slash("massCreation").withRel("massCreation")
                .withType("POST"));
        dto.add(linkTo(Objects.requireNonNull(methodOn(PersonController.class).update(dto))).withRel("update")
                .withType("PUT"));
        dto.add(linkTo(Objects.requireNonNull(methodOn(PersonController.class).disablePerson(dto.getId())))
                .withRel("disable").withType("PATCH"));
        dto.add(linkTo(Objects.requireNonNull(methodOn(PersonController.class).delete(dto.getId()))).withRel("delete")
                .withType("DELETE"));
    }
}
