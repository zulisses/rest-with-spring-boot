package com.github.zulisses.res_with_spring_boot.integrationtests.controllers.withjson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zulisses.res_with_spring_boot.config.TestConfigs;
import com.github.zulisses.res_with_spring_boot.data.dto.v1.security.AccountCredentialsDTO;
import com.github.zulisses.res_with_spring_boot.integrationtests.testcontainers.AbstractIntegrationTest;
import com.github.zulisses.res_with_spring_boot.integrationtests.dto.PersonDTO;
import com.github.zulisses.res_with_spring_boot.integrationtests.dto.wrappers.WrapperPersonDTO;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonControllerJsonTest extends AbstractIntegrationTest{
    
    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static String accessToken;

    private static PersonDTO person;

    @BeforeAll 
    public static void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        person = new PersonDTO();
    }

    
    @Test
    @Order(0)
    public void authorization() {
        AccountCredentialsDTO user = new AccountCredentialsDTO();
        user.setUsername("leandro");
        user.setPassword("admin123");

        accessToken = given()
            .basePath("/auth/signin")
            .port(TestConfigs.SERVER_PORT)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(user)
            .when()
            .post()
            .then()
            .statusCode(200)
            .extract()
            .path("accessToken");
    }

    @Test
    @Order(1)
    public void createTest() throws JsonProcessingException {
        mockPerson();
        
        specification = new RequestSpecBuilder()
        .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
        .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
        .setBasePath("/api/person/v1").setPort(TestConfigs.SERVER_PORT)
        .addFilter(new RequestLoggingFilter(LogDetail.ALL))
        .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
        .build();
        
        var content = given(specification)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(person)
        .when()
        .post()
        .then()
        .statusCode(200)
        .extract()
        .body().asString();
        
        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
        person = createdPerson;
        
        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() > 0);
        assertEquals("Ulisses", createdPerson.getFirstName());
        assertEquals("Carvalho", createdPerson.getLastName());
        assertEquals("BH", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertEquals(true, createdPerson.getEnabled());
    }

    @Test
    @Order(2)
    public void updateTest() throws JsonProcessingException {
        person.setLastName("Andrade Carvalho");

        var content = given(specification)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(person)
        .when()
        .put()
        .then()
        .statusCode(200)
        .extract()
        .body().asString();

        PersonDTO updatedPerson = objectMapper.readValue(content, PersonDTO.class);
        person = updatedPerson;
        
        assertNotNull(updatedPerson.getId());
        assertTrue(updatedPerson.getId() > 0);
        assertEquals("Ulisses", updatedPerson.getFirstName());
        assertEquals("Andrade Carvalho", updatedPerson.getLastName());
        assertEquals("BH", updatedPerson.getAddress());
        assertEquals("Male", updatedPerson.getGender());
        assertEquals(true, updatedPerson.getEnabled());
    }

    @Test
    @Order(3)
    public void findByIdTest() throws JsonProcessingException {
        
        var content = given(specification)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .pathParam("id", person.getId())
        .when()
        .get("/{id}")
        .then()
        .statusCode(200)
        .extract()
        .body().asString();

        PersonDTO foundPerson = objectMapper.readValue(content, PersonDTO.class);
        
        assertNotNull(foundPerson.getId());
        assertTrue(foundPerson.getId() > 0);
        assertEquals("Ulisses", foundPerson.getFirstName());
        assertEquals("Andrade Carvalho", foundPerson.getLastName());
        assertEquals("BH", foundPerson.getAddress());
        assertEquals("Male", foundPerson.getGender());
        assertEquals(true, foundPerson.getEnabled());
    }

    @Test
    @Order(4)
    public void disableTest() throws JsonProcessingException {
        
        var content = given(specification)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .pathParam("id", person.getId())
        .when()
        .patch("/{id}")
        .then()
        .statusCode(200)
        .extract()
        .body().asString();

        PersonDTO foundPerson = objectMapper.readValue(content, PersonDTO.class);
        
        assertNotNull(foundPerson.getId());
        assertTrue(foundPerson.getId() > 0);
        assertEquals("Ulisses", foundPerson.getFirstName());
        assertEquals("Andrade Carvalho", foundPerson.getLastName());
        assertEquals("BH", foundPerson.getAddress());
        assertEquals("Male", foundPerson.getGender());
        assertEquals(false, foundPerson.getEnabled());
    }

    @Test
    @Order(5)
    public void deleteTest() throws JsonProcessingException {
        
        given(specification)
        .pathParam("id", person.getId())
        .when()
        .delete("/{id}")
        .then()
        .statusCode(204);

    }

    @Test
    @Order(6)
    public void findAllTest() throws JsonProcessingException {
        
        var content = given(specification)
        .accept(MediaType.APPLICATION_JSON_VALUE)
        .queryParam("page", 3, "size", 12, "direction", "asc")
        .when()
        .get()
        .then()
        .statusCode(200)
        .extract()
        .body().asString();

        WrapperPersonDTO wrapper = objectMapper.readValue(content, WrapperPersonDTO.class);

        List<PersonDTO> people = wrapper.getEmbedded().getPeople();
        
        assertNotNull(people);
        assertTrue(people.size() > 0);
        assertEquals("Alvie", people.get(0).getFirstName());
        assertEquals("Masham", people.get(0).getLastName());
        assertEquals("Apt 1227", people.get(0).getAddress());
        assertEquals("Male", people.get(0).getGender());
        assertEquals(true, people.get(0).getEnabled());
    }

    @Test
    @Order(7)
    public void findPeopleByFirstNameTest() throws JsonProcessingException {
        
        var content = given(specification)
        .accept(MediaType.APPLICATION_JSON_VALUE)
        .queryParam("page", 0, "size", 12, "direction", "asc")
        .pathParam("firstName", "and")
        .when()
        .get("/findPeopleByFirstName/{firstName}")
        .then()
        .statusCode(200)
        .extract()
        .body().asString();

        WrapperPersonDTO wrapper = objectMapper.readValue(content, WrapperPersonDTO.class);

        List<PersonDTO> people = wrapper.getEmbedded().getPeople();
        
        assertNotNull(people);
        assertTrue(people.size() > 0);
        assertEquals("Aland", people.get(0).getFirstName());
        assertEquals("Blow", people.get(0).getLastName());
        assertEquals("PO Box 36999", people.get(0).getAddress());
        assertEquals("Male", people.get(0).getGender());
        assertEquals(false, people.get(0).getEnabled());
    }

    private void mockPerson() {
        person.setFirstName("Ulisses");
        person.setLastName("Carvalho");
        person.setAddress("BH");
        person.setGender("Male");
        person.setEnabled(true);
    }
}
