package com.sushmita.jms.basic.pub_sub.parallel_processing.durable_consumer;

import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;


// It is a durable subscriber
public class AlertApp {
    public static void main(String[] args) {
        InitialContext ic = null;
        try {
            ic = new InitialContext();

            //And look up the Queue:

            Topic cardTopic = (Topic) ic.lookup("topics/CardTopic");

            try(ActiveMQJMSConnectionFactory factory = new ActiveMQJMSConnectionFactory();
                JMSContext context = factory.createContext()) {
                String consumerID = "durable_consumer";
                String subscriptionName = "durable_consumer";

                context.setClientID(consumerID);
                //And we create a MessageConsumers which will consume orders from the queue: and we consume the message:
                JMSConsumer consumer = context.createDurableConsumer(cardTopic, subscriptionName);
                consumer.close();

                System.out.println("System will go to sleep!");
                Thread.sleep(10000);


                JMSConsumer consumerAfterSleep = context.createDurableConsumer(cardTopic, subscriptionName);

                for(int i=0;i<10;i++) {
                    System.out.println("Got card: "+ consumerAfterSleep.receiveBody(String.class));
                }

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
