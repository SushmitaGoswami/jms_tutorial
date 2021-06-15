package com.sushmita.jms.basic.security;

import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;

import javax.jms.JMSContext;
import javax.jms.JMSRuntimeException;
import javax.jms.Queue;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Producer {
    private static final String userName = "secureUser";
    private static final String password = "password";

    public static void main(String[] args) {
        InitialContext ic = null;
        try {
            ic = new InitialContext();

            //And look up the Queue:

            Queue orderQueue = (Queue) ic.lookup("queues/SecureQueue");

            try(ActiveMQJMSConnectionFactory factory = new ActiveMQJMSConnectionFactory();
                JMSContext context = factory.createContext()) {
                //We create a MessageProducer that will send orders to the queue:

                String message = "This is an order";
                context.createProducer().send(orderQueue, message);
                System.out.println("Sent order: " + message);
            }catch (JMSRuntimeException ex){
                System.out.println("It was expected as the topic is now secure!");
            }

            Thread.sleep(10000);

            try(ActiveMQJMSConnectionFactory factory = new ActiveMQJMSConnectionFactory();
                JMSContext context = factory.createContext(userName, password)) {
                //We create a MessageProducer that will send orders to the queue:

                String message = "This is an order";
                context.createProducer().send(orderQueue, message);
                System.out.println("Sent order: " + message);
            }

        }catch (NamingException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
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
