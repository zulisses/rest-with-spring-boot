package com.github.zulisses.res_with_spring_boot.integrationtests.swagger;



import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.github.zulisses.res_with_spring_boot.integrationtests.testcontainers.AbstractIntegrationTest;
import com.github.zulisses.res_with_spring_boot.config.TestConfigs;
import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SwaggerIntegrationTest extends AbstractIntegrationTest{

    @Test 
    public void shouldDisplaySwaggerUiPage() {
        var content = given()
            .basePath("/swagger-ui/index.html")
            .port(TestConfigs.SERVER_PORT)
            .when()
            .get()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString();
        
        assertTrue(content.contains("Swagger UI"));
    }
}
