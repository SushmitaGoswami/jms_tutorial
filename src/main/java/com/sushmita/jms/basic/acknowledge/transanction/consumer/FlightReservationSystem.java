package com.sushmita.jms.basic.acknowledge.transanction.consumer;

import javax.naming.NamingException;

public class FlightReservationSystem {
    public static void main(String[] args) {
        try {
            Consumer consumer = new Consumer();
            Thread.sleep(30000);
            consumer.close();
        } catch (NamingException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}
