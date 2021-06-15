package com.sushmita.jms.basic.pub_sub.parallel_processing.producer;

import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;

import javax.jms.JMSContext;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class CardApp {
    public static void main(String[] args) {
        InitialContext ic = null;
        try {
            ic = new InitialContext();

            //And look up the Queue:

            Topic cardTopic = (Topic) ic.lookup("topics/CardTopic");

            try(ActiveMQJMSConnectionFactory factory = new ActiveMQJMSConnectionFactory();
                JMSContext context = factory.createContext()) {
                //We create a MessageProducer that will send orders to the queue:

                String message = "Here is a card";
                for(int i=0; i<10; i++) {
                    context.createProducer().send(cardTopic, message);
                    System.out.println("Card : " + message);
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
