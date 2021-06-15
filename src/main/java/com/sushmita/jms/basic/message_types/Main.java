package com.sushmita.jms.basic.message_types;

import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.HashMap;

public class Main {

    public static void main(String [] args){
        InitialContext ic = null;
        try {
            ic = new InitialContext();

            //And look up the Queue:

            Queue orderQueue = (Queue) ic.lookup("queues/OrderQueue");

            try(ActiveMQJMSConnectionFactory factory = new ActiveMQJMSConnectionFactory();
                JMSContext context = factory.createContext()) {
                JMSConsumer consumer = context.createConsumer(orderQueue);
                JMSProducer producer = context.createProducer();

                // TEXT MESSAGE
                //We create a MessageProducer that will send orders to the queue:
                String message = "This is an order for text message";
                producer.send(orderQueue, message);
                System.out.println("Order Dispatched: " + message);

                Thread.sleep(1000);

                //And we create a MessageConsumer which will consume orders from the queue:
                String receivedMessage = consumer.receiveBody(String.class);
                //And we consume the message:
                System.out.println("Got order: " + receivedMessage);

                Thread.sleep(1000);

                // BYTE MESSAGE
                //We create a MessageProducer that will send orders to the queue:
                BytesMessage bytesMessage = context.createBytesMessage();
                bytesMessage.writeUTF("This is an order for byte message");

                producer.send(orderQueue, bytesMessage);
                System.out.println("Order Dispatched: " + "This is an order for byte message");

                Thread.sleep(1000);

                //And we create a MessageConsumer which will consume orders from the queue:
                BytesMessage receive = (BytesMessage)consumer.receive();
                //And we consume the message:
                System.out.println("Got order: " + receive.readUTF());

                Thread.sleep(1000);

                // MAP MESSAGE
                //We create a MessageProducer that will send orders to the queue:
                HashMap<Integer, String> map = new HashMap<>();
                map.put(1, "This is an order for map message");

                producer.send(orderQueue, map);
                map.forEach((k,v)-> System.out.println("Order Dispatched: " + v));

                Thread.sleep(1000);

                //And we create a MessageConsumer which will consume orders from the queue:
                HashMap receivedMap = consumer.receiveBody(HashMap.class);
                //And we consume the message:
                receivedMap.forEach((k,v)-> System.out.println("Got Order: " + v));

                Thread.sleep(1000);

                // Stream MESSAGE
                StreamMessage msg = context.createStreamMessage();
                msg.writeBytes("This is stream msg".getBytes());
                msg.writeBoolean(true);

                producer.send(orderQueue, msg);
                System.out.println("Order Dispatched: " + msg);

                Thread.sleep(1000);
                //And we create a MessageConsumer which will consume orders from the queue:
                StreamMessage streamMessage = consumer.receiveBody(StreamMessage.class);
                //And we consume the message:
                byte [] bytes = new byte[2000];
                System.out.println("Got bytes Order: " + streamMessage.readBytes(bytes));
                System.out.println("Got boolean Order: " + streamMessage.readBoolean());

                //OBJECT MESSAGE
                //We create a MessageProducer that will send orders to the queue:
                SampleOrder order = new SampleOrder(1, "This is an order for object message");

                producer.send(orderQueue, order);
                System.out.println("Order Dispatched: " + order);

                Thread.sleep(1000);

                //And we create a MessageConsumer which will consume orders from the queue:
                SampleOrder receivedOrder = consumer.receiveBody(SampleOrder.class);
                //And we consume the message:
                System.out.println("Got Order: " + receivedOrder);

                Thread.sleep(1000);
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
