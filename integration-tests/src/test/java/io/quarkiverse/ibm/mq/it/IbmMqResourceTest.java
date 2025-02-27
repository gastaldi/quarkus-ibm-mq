package io.quarkiverse.ibm.mq.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class IbmMqResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("/ibm-mq")
                .then()
                .statusCode(200)
                .body(is("Hello ibm-mq"));
    }
}
