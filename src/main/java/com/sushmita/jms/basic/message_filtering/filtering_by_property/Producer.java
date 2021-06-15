package com.sushmita.jms.basic.message_filtering.filtering_by_property;

import com.sushmita.jms.basic.message_filtering.model.Claim;
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

            Topic orderTopic = (Topic) ic.lookup("topics/OrderTopic");

            try(ActiveMQJMSConnectionFactory factory = new ActiveMQJMSConnectionFactory();
                JMSContext context = factory.createContext()) {
                Claim claim = new Claim();
                claim.setHospitalId(12);
                claim.setClaimAmount(1000d);
                claim.setDoctorName("John");
                claim.setDoctorType("cardio");
                claim.setInsuranceProvider("United Health");

                //We create a MessageProducer that will send orders to the queue:

                // check the equal operator
                JMSProducer producer = context.createProducer();

                ObjectMessage objectMessage = context.createObjectMessage();
                objectMessage.setIntProperty("hospitalID", 1);
                objectMessage.setObject(claim);

                producer.send(orderTopic, objectMessage);
                System.out.println("Order Dispatched: " + claim);

                Thread.sleep(3000);

                // check the BETWEEN with claimAmount
                ObjectMessage objectMessage2 = context.createObjectMessage();
                objectMessage2.setDoubleProperty("claimAmount", 1200d);
                objectMessage2.setObject(claim);

                producer.send(orderTopic, objectMessage2);
                System.out.println("Order Dispatched: " + claim);

                Thread.sleep(3000);

                // check the IN clause
                ObjectMessage objectMessage3 = context.createObjectMessage();
                objectMessage3.setStringProperty("doctorType", "gyna");
                objectMessage3.setObject(claim);

                producer.send(orderTopic, objectMessage3);
                System.out.println("Order Dispatched: " + claim);

                Thread.sleep(3000);

                // change the LIKE Clause
                ObjectMessage objectMessage4 = context.createObjectMessage();
                objectMessage4.setStringProperty("insuranceProvider", "Blue Shield");
                objectMessage4.setObject(claim);

                producer.send(orderTopic, objectMessage4);
                System.out.println("Order Dispatched: " + claim);



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
