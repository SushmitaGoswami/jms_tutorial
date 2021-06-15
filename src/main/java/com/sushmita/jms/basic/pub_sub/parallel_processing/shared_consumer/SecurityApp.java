package com.sushmita.jms.basic.pub_sub.parallel_processing.shared_consumer;

import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;


// It is a durable subscriber
public class SecurityApp {
    public static void main(String[] args) {
        InitialContext ic = null;
        try {
            ic = new InitialContext();

            //And look up the Queue:

            Topic cardTopic = (Topic) ic.lookup("topics/CardTopic");

            try(ActiveMQJMSConnectionFactory factory = new ActiveMQJMSConnectionFactory();
                JMSContext context = factory.createContext()) {
                String consumerID = "shared_consumer";
                String subscriptionName = "shared_consumer";

                context.setClientID(consumerID);
                //And we create a MessageConsumers which will consume orders from the queue: and we consume the message:
                JMSConsumer consumer1 = context.createSharedConsumer(cardTopic, subscriptionName);
                JMSConsumer consumer2 = context.createSharedConsumer(cardTopic, subscriptionName);

                for(int i=0; i<10; i+=2) {
                    System.out.println("Got card consumer 1: " + consumer1.receiveBody(String.class));
                    System.out.println("Got card consumer 2: " + consumer2.receiveBody(String.class));
                }

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
