package com.sushmita.jms.basic.pub_sub.jms_2_0;

import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;

import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Main {
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

                //And we create a MessageConsumers which will consume orders from the queue: and we consume the message:

                String receivedMessage1 = context.createConsumer(orderTopic).receiveBody(String.class);
                String receivedMessage2 = context.createConsumer(orderTopic).receiveBody(String.class);
                System.out.println("Got order: " + receivedMessage1);
                System.out.println("Got order: " + receivedMessage2);

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
