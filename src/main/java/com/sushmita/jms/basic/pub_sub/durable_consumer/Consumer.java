package com.sushmita.jms.basic.pub_sub.durable_consumer;

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
                String consumerID = "consumer";
                String subscriptionName = "durable_consumer";

                context.setClientID(consumerID);
                //And we create a MessageConsumers which will consume orders from the queue: and we consume the message:
                JMSConsumer consumer = context.createDurableConsumer(orderTopic, subscriptionName);
                consumer.close();

                System.out.println("System will go to sleep!");
                Thread.sleep(10000);

                JMSConsumer consumerAfterSleep = context.createDurableConsumer(orderTopic, subscriptionName);

                String receivedMessage1 = consumerAfterSleep.receiveBody(String.class);
                System.out.println("Got order: " + receivedMessage1);

            } catch (InterruptedException e) {
                e.printStackTrace();
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
