package com.sushmita.jms.basic.message_grouping;

import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.lang.IllegalStateException;
import java.util.concurrent.ConcurrentHashMap;

public class Consumer {
    public static void main(String[] args) {
        InitialContext ic = null;
        try {
            ic = new InitialContext();
            //And look up the Queue:

            Queue orderQueue = (Queue) ic.lookup("queues/OrderQueue");
            ConcurrentHashMap<String, String> hashMap = new ConcurrentHashMap<>();
            try(ActiveMQJMSConnectionFactory factory = new ActiveMQJMSConnectionFactory();
                JMSContext context = factory.createContext()) {
                //And we create a MessageConsumers which will consume orders from the queue: and we consume the message:
                JMSConsumer consumer1 = context.createConsumer(orderQueue);
                JMSConsumer consumer2 = context.createConsumer(orderQueue);

                CustomMessageListener customMessageListener1 = new CustomMessageListener("consumer1", hashMap);
                CustomMessageListener customMessageListener2 = new CustomMessageListener("consumer2", hashMap);

                consumer1.setMessageListener(customMessageListener1);
                consumer2.setMessageListener(customMessageListener2);

                Thread.sleep(30000);

                System.out.println(hashMap.size());
                hashMap.forEach((K,V) -> {
                    if(!V.equals("consumer1")){
                        throw new IllegalStateException("Msg: " + K +" read by wrong consumer: " + V);
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }catch (NamingException e) {
            e.printStackTrace();
        }  finally {
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
