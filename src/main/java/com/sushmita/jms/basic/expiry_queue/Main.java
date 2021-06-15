package com.sushmita.jms.basic.expiry_queue;

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
            Queue expiryQueue = (Queue) ic.lookup("queues/ExpiryQueue");

            try(ActiveMQJMSConnectionFactory factory = new ActiveMQJMSConnectionFactory();
                JMSContext context = factory.createContext()) {
                //We create a MessageProducer that will send orders to the queue:

                String msg = "This is an order";

                JMSProducer producer = context.createProducer();
                producer.setTimeToLive(1000);
                producer.send(orderQueue, msg);
                System.out.println("Order Dispatched: " + msg);

                Thread.sleep(2000);

                //And we create a MessageConsumer which will consume orders from the queue:
                String receivedMessage = context.createConsumer(orderQueue).receiveBody(String.class, 2000);

                if(receivedMessage == null){
                    System.out.println("No order received, trying to find in the expiry queue!");
                    receivedMessage = context.createConsumer(expiryQueue).receiveBody(String.class, 2000);
                }
                //And we consume the message:
                System.out.println("Got order: " + receivedMessage);

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
