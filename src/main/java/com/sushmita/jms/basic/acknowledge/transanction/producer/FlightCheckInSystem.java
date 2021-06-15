package com.sushmita.jms.basic.acknowledge.transanction.producer;

import javax.naming.NamingException;

public class FlightCheckInSystem {
    public static void main(String[] args) {
        try {
            Producer producer = new Producer();
            producer.send();
            producer.close();
        } catch (NamingException e) {
            e.printStackTrace();
        }

    }
}
