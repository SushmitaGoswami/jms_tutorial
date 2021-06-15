package com.sushmita.jms.basic.p2p.request_reply.consumer;

import javax.naming.NamingException;

public class Customer {
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
