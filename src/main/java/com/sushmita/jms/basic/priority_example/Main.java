package com.sushmita.jms.basic.priority_example;

import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Main {
    public static void main(String[] args) {
        InitialContext ic = null;
        try {
            ic = new InitialContext();

            //And look up the Queue:

            Queue orderQueue = (Queue) ic.lookup("queues/OrderQueue");

            try(ActiveMQJMSConnectionFactory factory = new ActiveMQJMSConnectionFactory();
                JMSContext context = factory.createContext()) {

                //We create a MessageProducer that will send orders to the queue:
                JMSProducer producer = context.createProducer();

                // Prepare messages to send
                String [] messages = {"Stay Safe!","Be happy","Do regular exercise"};
                Integer [] priorities = {1, 3, 9};

                // send the messages
                for(int i = 0; i<messages.length; i++){
                    producer.setPriority(priorities[i]);
                    producer.send(orderQueue, messages[i]);
                    System.out.println("Order Dispatched: " +messages[i]);
                }
                //And we create a MessageConsumer which will consume orders from the queue:
                JMSConsumer consumer = context.createConsumer(orderQueue);
                for(int i=0; i<messages.length; i++) {
                    //And we consume the message:
                    System.out.println("Got order: " + consumer.receiveBody(String.class));
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
