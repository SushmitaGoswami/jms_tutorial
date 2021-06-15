package com.sushmita.jms.basic.message_types;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SampleOrder implements Serializable {
    private int orderID;
    private String name;
    private Date dateOfOrder;

    public SampleOrder(int orderID, String name){
        this.orderID = orderID;
        this.name = name;
        this.dateOfOrder = new Date();
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateOfOrder() {
        return dateOfOrder;
    }

    public void setDateOfOrder(Date dateOfOrder) {
        this.dateOfOrder = dateOfOrder;
    }

    @Override
    public String toString() {
        return "SampleOrder{" +
                "orderID=" + orderID +
                ", name='" + name + '\'' +
                ", dateOfOrder=" + new SimpleDateFormat("\"yyyy.MM.dd G 'at' HH:mm:ss z\"").format(dateOfOrder) +
                '}';
    }
}
