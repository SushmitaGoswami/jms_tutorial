package com.sushmita.jms.basic.custom_property;

import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Main {
    public static void main(String[] args) {
        InitialContext ic = null;
        try {
            ic = new InitialContext();

            // And look up the Topic:

            Queue orderQueue = (Queue) ic.lookup("queues/OrderQueue");

            try(ActiveMQJMSConnectionFactory factory = new ActiveMQJMSConnectionFactory();
                JMSContext context = factory.createContext()) {

                String msg = "This is an text order";
                TextMessage message = context.createTextMessage();
                message.setText(msg);
                message.setIntProperty("id", 1);
                message.setStringProperty("name", msg);

                // We create a MessageProducer that will send orders to the queue:
                context.createProducer().send(orderQueue, message);
                System.out.println("Order Dispatched: " +
                                    "id: " + message.getIntProperty("id") + ", " +
                                    "name: " + message.getStringProperty("name"));

                Thread.sleep(2000);

                // And we create a MessageConsumer which will consume orders from the queue:
                TextMessage receivedMessage = (TextMessage) context.createConsumer(orderQueue).receive();


//                // And we consume the message:
                System.out.println("Got order: " +
                                   "id: " + receivedMessage.getIntProperty("id") + ", " +
                                   "name: " + receivedMessage.getStringProperty("name")
                );

            } catch (JMSException | InterruptedException e) {
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
