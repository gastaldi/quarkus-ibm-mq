package io.quarkiverse.ibm.mq.it;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@QuarkusTest
public class IbmMqResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("/ibm-mq")
                .then()
                .statusCode(200)
                .body(is("Hello ibm-mq"));
        given()
                .when().get("/ibm-mq/receive")
                .then()
                .statusCode(200)
                .body(is("Goodbye"));
    }
}
