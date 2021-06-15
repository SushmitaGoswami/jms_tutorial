package com.sushmita.jms.basic.p2p.queuebrowser;

import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Enumeration;

public class Main {
    public static void main(String[] args) {
        InitialContext ic = null;
        QueueBrowser browser = null;
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

                // Browse the queue
                //Creates a QueueBrowser:
                browser = context.createBrowser(orderQueue);

                //Retrieves the Enumeration that contains the messages:
                Enumeration msgs = browser.getEnumeration();

                //Verifies that the Enumeration contains messages, then displays the contents of the messages:
                if ( !msgs.hasMoreElements() ) {
                    System.out.println("No messages in queue");
                } else {
                    while (msgs.hasMoreElements()) {
                        Message tempMsg = (TextMessage)msgs.nextElement();
                        System.out.println("Message: " + ((TextMessage) tempMsg).getText());
                    }
                }

                //And we create a MessageConsumer which will consume orders from the queue:

                String receivedMessage = context.createConsumer(orderQueue).receiveBody(String.class);

                //And we consume the message:

                System.out.println("Got order: " + receivedMessage);

            } catch (JMSException e) {
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
                if(browser != null){
                    browser.close();
                }
            } catch (NamingException | JMSException e) {
                e.printStackTrace();
            }
        }
    }
}
