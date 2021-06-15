package com.sushmita.jms.basic.p2p.request_reply.consumer;

import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Consumer implements MessageListener {

    private InitialContext initialContext;
    private Topic orderTopic;
    private JMSProducer replyProducer;
    private JMSContext context;

    public Consumer() throws NamingException {
        initialize();
    }
    public void initialize() throws NamingException {
        initialContext = new InitialContext();

        //And look up the Queue:
        orderTopic = (Topic) initialContext.lookup("topics/OrderTopic");

        ActiveMQJMSConnectionFactory factory = new ActiveMQJMSConnectionFactory();
        context = factory.createContext();
        JMSConsumer consumer = context.createConsumer(orderTopic);
        consumer.setMessageListener(this);
        replyProducer = context.createProducer();
    }

    @Override
    public void onMessage(Message message) {
        try {
            TextMessage receivedMessage = (TextMessage) message;

            // And we consume the message:
            System.out.println("Got order: " + receivedMessage.getText());

            // Send the reply back to the replyConsumer
            TextMessage replyMessage = context.createTextMessage();
            replyMessage.setText("I have received the order");
            replyMessage.setJMSCorrelationID(receivedMessage.getJMSMessageID());
            replyProducer.send(receivedMessage.getJMSReplyTo(), replyMessage);
            System.out.println("Send from consumer end " + replyMessage.getJMSCorrelationID());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        try {
         if(initialContext != null) {
             initialContext.close();
         }
         if(context != null){
             context.close();
         }
         } catch (NamingException e) {
             e.printStackTrace();
         }
     }
}

