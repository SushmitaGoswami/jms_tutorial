package com.sushmita.jms.basic.p2p.jms_2_0;

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

                String message = "This is an order";
                context.createProducer().send(orderQueue, message);
                System.out.println("Order Dispatched: " + message);

                //And we create a MessageConsumer which will consume orders from the queue:

                String receivedMessage = context.createConsumer(orderQueue).receiveBody(String.class);

                //And we consume the message:


                System.out.println("Got order: " + receivedMessage);

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
