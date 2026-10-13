package com.github.zulisses.res_with_spring_boot.integrationtests.controllers.cors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import com.github.zulisses.res_with_spring_boot.integrationtests.dto.PersonDTO;
import com.github.zulisses.res_with_spring_boot.integrationtests.testcontainers.AbstractIntegrationTest;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonControllerCorsTest extends AbstractIntegrationTest{
    
    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;

    private static PersonDTO person;

    @BeforeAll 
    public static void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        person = new PersonDTO();
    }

    
    @Test
    @Order(1)
    public void create() throws JsonProcessingException {
        mockPerson();
        
        specification = new RequestSpecBuilder().addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
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
        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getAddress());
        assertNotNull(createdPerson.getGender());
        
        assertTrue(createdPerson.getId() > 0);
        assertEquals(person.getFirstName(), createdPerson.getFirstName());
        assertEquals(person.getLastName(), createdPerson.getLastName());
        assertEquals(person.getAddress(), createdPerson.getAddress());
        assertEquals(person.getGender(), createdPerson.getGender());
        assertEquals(true, createdPerson.getEnabled());
    }

    @Test
    @Order(2)
    public void createWithWrongOrigin() throws JsonProcessingException {
        specification = new RequestSpecBuilder().addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_SEMERU)
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
        .statusCode(403)
        .extract()
        .body().asString();
        
        assertEquals("Invalid CORS request", content);
    }

    @Test
    @Order(3)
    public void findById() throws JsonProcessingException {
        specification = new RequestSpecBuilder().addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LOCAL)
        .setBasePath("/api/person/v1").setPort(TestConfigs.SERVER_PORT)
        .addFilter(new RequestLoggingFilter(LogDetail.ALL))
        .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
        .build();
        
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
        assertNotNull(foundPerson.getFirstName());
        assertNotNull(foundPerson.getLastName());
        assertNotNull(foundPerson.getAddress());
        assertNotNull(foundPerson.getGender());
        
        assertTrue(foundPerson.getId() > 0);
        assertEquals(person.getFirstName(), foundPerson.getFirstName());
        assertEquals(person.getLastName(), foundPerson.getLastName());
        assertEquals(person.getAddress(), foundPerson.getAddress());
        assertEquals(person.getGender(), foundPerson.getGender());
        assertEquals(true, foundPerson.getEnabled());
    }

    @Test
    @Order(4)
    public void findByIdWithWrongOrigin() throws JsonProcessingException {
        specification = new RequestSpecBuilder().addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_SEMERU)
        .setBasePath("/api/person/v1").setPort(TestConfigs.SERVER_PORT)
        .addFilter(new RequestLoggingFilter(LogDetail.ALL))
        .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
        .build();
        
        var content = given(specification)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .pathParam("id", person.getId())
        .when()
        .get("/{id}")
        .then()
        .statusCode(403)
        .extract()
        .body().asString();

        assertEquals("Invalid CORS request", content);
    }

    private void mockPerson() {
        person.setFirstName("Ulisses");
        person.setLastName("Carvalho");
        person.setAddress("BH");
        person.setGender("Male");
        person.setEnabled(true);
    }
}
