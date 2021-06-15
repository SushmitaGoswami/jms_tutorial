package com.sushmita.jms.basic.message_grouping;

import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Producer {

    public static void main(String[] args) {
        InitialContext ic = null;
        try {
            ic = new InitialContext();

            //And look up the Queue:
            Queue orderQueue = (Queue) ic.lookup("queues/OrderQueue");

            try(ActiveMQJMSConnectionFactory factory = new ActiveMQJMSConnectionFactory();
                JMSContext context = factory.createContext()) {
                //We create a MessageProducer that will send orders to the queue:

                TextMessage[] txtMessages = new TextMessage[10];
                for(int i=0; i<10; i++) {
                    txtMessages[i] = context.createTextMessage("Group-0 message" + i);
                    txtMessages[i].setStringProperty("JMSXGroupID", "Group-0");
                    context.createProducer().send(orderQueue, txtMessages[i]);
                    System.out.println("Sent order: " + txtMessages[i].getText());
                }
            }catch (JMSException e) {
                e.printStackTrace();
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
