/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.quarkiverse.ibm.mq.it;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSContext;
import jakarta.jms.JMSException;
import jakarta.jms.Queue;
import jakarta.jms.TextMessage;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/ibm-mq")
@ApplicationScoped
public class IbmMqResource {
    // add some rest methods here

    @Inject
    ConnectionFactory connectionFactory;

    @GET
    public String hello() {
        try (JMSContext context = connectionFactory.createContext()) {
            Queue queue = context.createQueue("DEV.QUEUE.1");
            TextMessage textMessage = context.createTextMessage("Hello");
            textMessage.setJMSReplyTo(context.createQueue("DEV.QUEUE.2"));
            context.createProducer().send(queue, textMessage);
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
        return "Hello ibm-mq";
    }
}
