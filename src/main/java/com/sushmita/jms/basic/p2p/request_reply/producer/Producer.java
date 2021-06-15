package com.sushmita.jms.basic.p2p.request_reply.producer;

import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Producer {

    private InitialContext initialContext;
    private Topic requestTopic;
    private Topic responseTopic;
    private JMSProducer producer;
    private JMSContext context;
    private JMSConsumer jmsConsumer;

    public Producer() throws NamingException {
        initialize();
    }
    public void initialize() throws NamingException {
        initialContext = new InitialContext();


        ActiveMQJMSConnectionFactory factory = new ActiveMQJMSConnectionFactory();
        context = factory.createContext();

        //And look up the Queue:
        requestTopic = (Topic) initialContext.lookup("topics/OrderTopic");
        responseTopic = context.createTemporaryTopic();

        producer = context.createProducer();
        jmsConsumer = context.createConsumer(responseTopic);
    }

    public void send() {
        try {
            // Create the message
            TextMessage message = context.createTextMessage();
            message.setText("This is an order");
            message.setJMSReplyTo(responseTopic);

            // And send the message
            producer.send(requestTopic, message);
            System.out.println("Sent order: " + message.getText());

            TextMessage receivedMessage = (TextMessage) jmsConsumer.receive(15000);
            try {
                System.out.println("Received at producer end " + receivedMessage.getJMSCorrelationID());
            } catch (JMSException e) {
                e.printStackTrace();
            }
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
