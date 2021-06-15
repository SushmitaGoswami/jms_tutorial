package com.sushmita.jms.basic.p2p.request_reply.producer;

import javax.naming.NamingException;

public class Supplier {
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
