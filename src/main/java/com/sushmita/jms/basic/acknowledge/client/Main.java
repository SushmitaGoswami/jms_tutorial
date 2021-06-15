package com.sushmita.jms.basic.acknowledge.client;

import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.soap.Text;

public class Main {
    public static void main(String[] args) {
        InitialContext ic = null;
        try {
            ic = new InitialContext();

            //And look up the Queue:

            Queue orderQueue = (Queue) ic.lookup("queues/OrderQueue");

            try(ActiveMQJMSConnectionFactory factory = new ActiveMQJMSConnectionFactory();
                JMSContext context = factory.createContext(JMSContext.CLIENT_ACKNOWLEDGE)) {

                //We create a MessageProducer that will send orders to the queue:

                TextMessage message = context.createTextMessage();
                message.setText("This is an order");

                context.createProducer().send(orderQueue, message);
                message.acknowledge();
                System.out.println("Order Dispatched: " + message.getText());

                //And we create a MessageConsumer which will consume orders from the queue:
                JMSConsumer consumer = context.createConsumer(orderQueue);

                TextMessage receivedMessage = (TextMessage) consumer.receive();
                //And we consume the message:
                System.out.println("Got order: " + receivedMessage.getText());

                TextMessage receivedMessage2 = (TextMessage) consumer.receive();
                //And we consume the message:
                System.out.println("Got order: " + receivedMessage2.getText());


                // acknowledge the message receive
                receivedMessage.acknowledge();
                receivedMessage2.acknowledge();

                Thread.sleep(10000);

                TextMessage receivedMessage3 = (TextMessage) consumer.receive(1000);

                //And we try consume the message:
                String nullMessage = (receivedMessage3 == null) ? "NULL" : receivedMessage3.getText();
                System.out.println("Got order: " + nullMessage);

            } catch (JMSException e) {
                e.printStackTrace();
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
