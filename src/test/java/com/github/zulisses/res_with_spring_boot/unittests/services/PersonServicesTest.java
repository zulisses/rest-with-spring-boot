package com.github.zulisses.res_with_spring_boot.unittests.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.zulisses.res_with_spring_boot.data.dto.v1.PersonDTO;
import com.github.zulisses.res_with_spring_boot.exception.RequiredObjectIsNullException;
import com.github.zulisses.res_with_spring_boot.model.Person;
import com.github.zulisses.res_with_spring_boot.repository.PersonRepository;
import com.github.zulisses.res_with_spring_boot.services.PersonServices;
import com.github.zulisses.res_with_spring_boot.unittests.mapper.mocks.MockPerson;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PersonServicesTest {

    private MockPerson input;

    @InjectMocks 
    private PersonServices service;

    @Mock
    private PersonRepository repository;

    @BeforeEach
    void setUp() {
        input = new MockPerson();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findById() {
        Person person = input.mockEntity(0);
        when(repository.findById(0L)).thenReturn(Optional.of(person));
        PersonDTO result = service.findById(0L);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertNotNull(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("self") 
            && link.getHref().endsWith("/api/person/v1/1")
            && link.getType().equals("GET")));
        assertNotNull(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("findAll") 
            && link.getHref().endsWith("/api/person/v1")
            && link.getType().equals("GET")));
        assertNotNull(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("create") 
            && link.getHref().endsWith("/api/person/v1")
            && link.getType().equals("POST")));
        assertNotNull(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("update") 
            && link.getHref().endsWith("/api/person/v1")
            && link.getType().equals("PUT")));
        assertNotNull(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("delete") 
            && link.getHref().endsWith("/api/person/v1/1")
            && link.getType().equals("DELETE")));

        assertPersonDTO(result, input.mockDTO(0));
        verify(repository).findById(0L);
    }

    @Test
    void create() {
        Person person = input.mockEntity(1);
        Person persisted = person;
        persisted.setId(1L);

        PersonDTO dto = input.mockDTO(1);
        when(repository.save(person)).thenReturn(persisted);

        PersonDTO result = service.create(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertNotNull(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("self") 
            && link.getHref().endsWith("/api/person/v1/1")
            && link.getType().equals("GET")));
        assertNotNull(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("findAll") 
            && link.getHref().endsWith("/api/person/v1")
            && link.getType().equals("GET")));
        assertNotNull(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("create") 
            && link.getHref().endsWith("/api/person/v1")
            && link.getType().equals("POST")));
        assertNotNull(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("update") 
            && link.getHref().endsWith("/api/person/v1")
            && link.getType().equals("PUT")));
        assertNotNull(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("delete") 
            && link.getHref().endsWith("/api/person/v1/1")
            && link.getType().equals("DELETE")));

        assertPersonDTO(result, input.mockDTO(1));
        verify(repository).save(person);
    }

    @Test
    void update() {
        Person person = input.mockEntity(1);
        Person persisted = person;
        persisted.setId(1L);

        PersonDTO dto = input.mockDTO(1);
        when(repository.findById(1L)).thenReturn(Optional.of(person));
        when(repository.save(person)).thenReturn(persisted);

        PersonDTO result = service.update(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertNotNull(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("self") 
            && link.getHref().endsWith("/api/person/v1/1")
            && link.getType().equals("GET")));
        assertNotNull(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("findAll") 
            && link.getHref().endsWith("/api/person/v1")
            && link.getType().equals("GET")));
        assertNotNull(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("create") 
            && link.getHref().endsWith("/api/person/v1")
            && link.getType().equals("POST")));
        assertNotNull(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("update") 
            && link.getHref().endsWith("/api/person/v1")
            && link.getType().equals("PUT")));
        assertNotNull(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("delete") 
            && link.getHref().endsWith("/api/person/v1/1")
            && link.getType().equals("DELETE")));

        assertPersonDTO(result, input.mockDTO(1));
        verify(repository).save(person);
    }

    @Test
    void delete() {
        Person person = input.mockEntity(0);
        when(repository.findById(0L)).thenReturn(Optional.of(person));
        service.delete(0L);

        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).delete(any(Person.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    void createWithNullPerson() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> service.create(null));
        assertEquals("It is not allowed to persist a null object", exception.getMessage());
    }

    @Test
    void updateWithNullPerson() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> service.update(null));
        assertEquals("It is not allowed to persist a null object", exception.getMessage());
    }

    @Test
    @Disabled("REASON: Still under Development")
    void findAll() {
        List<Person> list = input.mockEntityList();
        when(repository.findAll()).thenReturn(list);
        List<PersonDTO> people = new ArrayList<>(); // service.findAll(pegeable); 

        assertNotNull(people);
        assertEquals(14, people.size());
        
        var personOne = people.get(0);

        assertNotNull(personOne.getLinks().stream().anyMatch(link -> link.getRel().value().equals("self") 
            && link.getHref().endsWith("/api/person/v1/1")
            && link.getType().equals("GET")));
        assertNotNull(personOne.getLinks().stream().anyMatch(link -> link.getRel().value().equals("findAll") 
            && link.getHref().endsWith("/api/person/v1")
            && link.getType().equals("GET")));
        assertNotNull(personOne.getLinks().stream().anyMatch(link -> link.getRel().value().equals("create") 
            && link.getHref().endsWith("/api/person/v1")
            && link.getType().equals("POST")));
        assertNotNull(personOne.getLinks().stream().anyMatch(link -> link.getRel().value().equals("update") 
            && link.getHref().endsWith("/api/person/v1")
            && link.getType().equals("PUT")));
        assertNotNull(personOne.getLinks().stream().anyMatch(link -> link.getRel().value().equals("delete") 
            && link.getHref().endsWith("/api/person/v1/1")
            && link.getType().equals("DELETE")));

        assertPersonDTO(personOne, input.mockDTO(0));
    }

    private void assertPersonDTO(PersonDTO actual, PersonDTO expected) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getAddress(), actual.getAddress());
        assertEquals(expected.getGender(), actual.getGender());
    }
}
