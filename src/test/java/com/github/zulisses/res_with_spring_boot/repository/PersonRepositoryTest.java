package com.github.zulisses.res_with_spring_boot.repository;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.github.zulisses.res_with_spring_boot.integrationtests.testcontainers.AbstractIntegrationTest;
import com.github.zulisses.res_with_spring_boot.model.Person;

import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonRepositoryTest extends AbstractIntegrationTest{

    @Autowired
    private PersonRepository repository;
    private static Person person;

    @BeforeAll
    public static void setUp() {
        person = new Person();
    }

    @Test
    @Order(1)
    public void findPeopleByFirstNameTest() {
        Pageable pageable = PageRequest.of(0, 12, Sort.by(Direction.ASC, "firstName"));

        person = repository.findPeopleByFirstName("iko", pageable).getContent().get(0);

        assertNotNull(person);
        assertNotNull(person.getId());
        assertEquals("Nikola", person.getFirstName());
        assertEquals("Cowan", person.getLastName());
        assertEquals("Suite 43", person.getAddress());
        assertEquals("Male", person.getGender());
        assertEquals(true, person.isEnabled());
    }

    @Test 
    @Order(2)
    public void disablePersonTest() {
        Long id = person.getId();
        repository.disablePerson(id);

        person = repository.findById(id).get();

        assertNotNull(person);
        assertNotNull(person.getId());
        assertEquals("Nikola", person.getFirstName());
        assertEquals("Cowan", person.getLastName());
        assertEquals("Suite 43", person.getAddress());
        assertEquals("Male", person.getGender());
        assertEquals(false, person.isEnabled());
    }
}

