package com.sushmita.jms.basic.p2p.jms_1_0;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Main {

    public static void main(String [] args){
        InitialContext ic = null;
        Connection connection = null;
        Session session = null;
        try {
             ic = new InitialContext();

            //Now we'll look up the connection factory from which we can create connections to myhost:5445:

            ConnectionFactory cf = (ConnectionFactory) ic.lookup("ConnectionFactory");

            //And look up the Queue:

            Queue orderQueue = (Queue) ic.lookup("queues/OrderQueue");

            //Next we create a JMS connection using the connection factory:

            connection = cf.createConnection();

            //And we create a non transacted JMS Session, with AUTO_ACKNOWLEDGE acknowledge mode:

            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            //We create a MessageProducer that will send orders to the queue:

            MessageProducer producer = session.createProducer(orderQueue);

            //And we create a MessageConsumer which will consume orders from the queue:

            MessageConsumer consumer = session.createConsumer(orderQueue);

            //We make sure we start the connection, or delivery won't occur on it:

            connection.start();

            //We create a simple TextMessage and send it:

            TextMessage message = session.createTextMessage("This is an order");
            producer.send(message);

            System.out.println("Order Dispatched: "+ message.getText());
            //And we consume the message:

            TextMessage receivedMessage = (TextMessage) consumer.receive();
            System.out.println("Got order: " + receivedMessage.getText());

        }catch (NamingException |JMSException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                if (session != null) {
                    session.close();
                }
                if (ic != null) {
                    ic.close();
                }
            } catch (JMSException | NamingException e) {
                e.printStackTrace();
            }
        }
    }
}
