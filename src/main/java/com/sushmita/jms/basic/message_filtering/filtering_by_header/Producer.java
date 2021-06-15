package com.sushmita.jms.basic.message_filtering.filtering_by_header;

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

                producer.setPriority(5);

                producer.send(orderTopic, objectMessage);
                System.out.println("Order Dispatched: " + claim);

            } catch (JMSException e) {
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
