package com.sushmita.jms.basic.security;

import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Consumer {
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
                //And we create a MessageConsumers which will consume orders from the queue: and we consume the message:
                JMSConsumer consumer = context.createConsumer(orderQueue);
                String msg = consumer.receiveBody(String.class, 1000);
                System.out.println("Received at consumer end: " + msg);
            } catch (JMSRuntimeException ex){
                System.out.println("It was expected as the topic is now secure!");
            }

            System.out.println("Will try with proper credential to access the secure topic!");

            try(ActiveMQJMSConnectionFactory factory = new ActiveMQJMSConnectionFactory();
                JMSContext context = factory.createContext(userName, password)) {
                //And we create a MessageConsumers which will consume orders from the queue: and we consume the message:
                JMSConsumer consumer = context.createConsumer(orderQueue);
                String msg = consumer.receiveBody(String.class);
                System.out.println("Received at consumer end: " + msg);
                consumer.close();
            }
        }catch (NamingException e) {
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
