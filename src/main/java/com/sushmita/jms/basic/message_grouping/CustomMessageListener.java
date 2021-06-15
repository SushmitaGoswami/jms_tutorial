package com.sushmita.jms.basic.message_grouping;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.Map;

public class CustomMessageListener implements MessageListener {
    private String name;
    private Map<String, String> receivedMessages;

    public CustomMessageListener(String name, Map<String, String> receivedMessages){
        this.name = name;
        this.receivedMessages = receivedMessages;
    }


    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage)message;
        try {
            System.out.println("Received message: "+textMessage.getText());
            System.out.println("Listener name: " + name);
            receivedMessages.put(textMessage.getText(), name);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
