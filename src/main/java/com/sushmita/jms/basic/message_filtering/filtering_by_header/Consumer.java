package com.sushmita.jms.basic.message_filtering.filtering_by_header;

import com.sushmita.jms.basic.message_filtering.model.Claim;
import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Consumer {
    public static void main(String[] args) {
        InitialContext ic = null;
        try {
            ic = new InitialContext();

            //And look up the Queue:

            Topic orderTopic = (Topic) ic.lookup("topics/OrderTopic");

            try(ActiveMQJMSConnectionFactory factory = new ActiveMQJMSConnectionFactory();
                JMSContext context = factory.createContext()) {

                //And we create a MessageConsumers which will consume orders from the queue: and we consume the message:

                // consumer1 :- check the equal operator
                JMSConsumer consumer1 = context.createConsumer(orderTopic, "JMSPriority BETWEEN 3 AND 6");
                Claim claim1 = consumer1.receiveBody(Claim.class, 10000);
                System.out.println("Got order: " + claim1);
            }
        }catch (NamingException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (ic != null) {
                    ic.close();
                }
            } catch (NamingException e) {
                e.printStackTrace();
            }
        }
    }
}
