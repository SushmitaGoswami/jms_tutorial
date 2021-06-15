package com.sushmita.jms.basic.message_filtering.filtering_by_property;

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
                JMSConsumer consumer1 = context.createConsumer(orderTopic, "hospitalID=1");
                Claim claim1 = consumer1.receiveBody(Claim.class, 10000);
                System.out.println("Got order: " + claim1);

                // consumer2 :- check the BETWEEN operator
                JMSConsumer consumer2 = context.createConsumer(orderTopic, "claimAmount BETWEEN 1000 AND 1500");
                Claim claim2 = consumer2.receiveBody(Claim.class, 10000);
                System.out.println("Got order: " + claim2);

                // consumer3 :- check the IN operator
                JMSConsumer consumer3 = context.createConsumer(orderTopic, "doctorType IN ('gyna','cardio')");
                Claim claim3 = consumer3.receiveBody(Claim.class, 10000);
                System.out.println("Got order: " + claim3);

                // consumer4 :- check the LIKE operator
                JMSConsumer consumer4 = context.createConsumer(orderTopic, "insuranceProvider LIKE 'H%'");
                Claim claim4 = consumer4.receiveBody(Claim.class, 10000);
                System.out.println("Got order: " + claim4);
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
