package com.sushmita.jms.basic.pub_sub.durable_consumer;

import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;

import javax.jms.JMSContext;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Producer {
    public static void main(String[] args) {
        InitialContext ic = null;
        try {
            ic = new InitialContext();

            //And look up the Queue:

            Topic orderTopic = (Topic) ic.lookup("topics/OrderTopic");

            try(ActiveMQJMSConnectionFactory factory = new ActiveMQJMSConnectionFactory();
                JMSContext context = factory.createContext()) {
                //We create a MessageProducer that will send orders to the queue:

                String message = "This is an order";
                context.createProducer().send(orderTopic, message);
                System.out.println("Order Dispatched: " + message);
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
