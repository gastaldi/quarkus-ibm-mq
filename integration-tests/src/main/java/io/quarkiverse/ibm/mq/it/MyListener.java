package io.quarkiverse.ibm.mq.it;

import io.quarkiverse.ironjacamar.ResourceEndpoint;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;

@ResourceEndpoint(activationSpecConfigKey = "foo")
public class MyListener implements MessageListener {
    @Override
    public void onMessage(Message message) {
        try {
            System.out.println("Message: " + message.getBody(String.class));
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
