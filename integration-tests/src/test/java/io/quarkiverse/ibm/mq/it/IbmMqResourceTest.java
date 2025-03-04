package io.quarkiverse.ibm.mq.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.inject.Inject;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSConsumer;
import jakarta.jms.JMSContext;
import jakarta.jms.Queue;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class IbmMqResourceTest {

    @Inject
    ConnectionFactory connectionFactory;

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("/ibm-mq")
                .then()
                .statusCode(200)
                .body(is("Hello ibm-mq"));
        try (JMSContext context = connectionFactory.createContext()) {
            Queue queue = context.createQueue("DEV.QUEUE.2");
            JMSConsumer consumer = context.createConsumer(queue);
            String receivedBody = consumer.receiveBody(String.class);
            System.out.println("Received: " + receivedBody);
            assertEquals("Goodbye", receivedBody);
        }
    }
}
