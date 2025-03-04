package io.quarkiverse.ibm.mq.it;

import jakarta.inject.Inject;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSContext;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.transaction.Transactional;

import io.quarkiverse.ironjacamar.ResourceEndpoint;

@ResourceEndpoint(activationSpecConfigKey = "foo")
public class MyListener implements MessageListener {

    @Inject
    ConnectionFactory connectionFactory;

    @Override
    @Transactional
    public void onMessage(Message message) {
        try {
            System.out.println("Message: " + message.getBody(String.class));
            try (JMSContext context = connectionFactory.createContext()) {
                context.createProducer().send(message.getJMSReplyTo(), "Goodbye");
            }

        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
