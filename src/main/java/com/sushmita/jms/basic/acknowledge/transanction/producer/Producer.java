package com.sushmita.jms.basic.acknowledge.transanction.producer;

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
        context = factory.createContext(Session.SESSION_TRANSACTED);

        //And look up the Queue:
        requestTopic = (Topic) initialContext.lookup("topics/OrderTopic");
        responseTopic = context.createTemporaryTopic();

        producer = context.createProducer();
        jmsConsumer = context.createConsumer(responseTopic);
    }

    public void send() {
        try {

            sendAndAcknowledge("This is an order", true,false);
            sendAndAcknowledge("This is another order", false, false);
//            System.out.println("The next call will call a commit, so the previous msg will move from cache to the queue. " +
//                    "Consumer should receive the message now!");
            sendAndAcknowledge("This is for testing rollback", false, true);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private void sendAndAcknowledge(String message, boolean isCommit, boolean isRollback) throws JMSException {
        // Create the message
        TextMessage txtMessage = context.createTextMessage();
        txtMessage.setText(message);
        txtMessage.setJMSReplyTo(responseTopic);

        // And send the message
        producer.send(requestTopic, txtMessage);
        // Rolling back the transaction
        if(isRollback){
            context.rollback();
            System.out.println("Rolling back the order: " + txtMessage.getText());
        }
        else {
            if (isCommit) {
                context.commit();

                System.out.println("Sent order: " + txtMessage.getText());

                // Acknowledge
                TextMessage receivedMessage = (TextMessage) jmsConsumer.receive(15000);
                String msg = (receivedMessage == null) ? "No message received from consumer" : receivedMessage.getJMSCorrelationID();
                System.out.println("Received at producer end: " + msg);

                context.commit();
            }
            else{
                System.out.println("Sent order, but haven't committed: " + txtMessage.getText());
            }
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
