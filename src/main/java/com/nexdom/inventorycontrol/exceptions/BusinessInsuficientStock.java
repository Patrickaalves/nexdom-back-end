package com.nexdom.inventorycontrol.exceptions;

public class BusinessInsuficientStock extends RuntimeException {
    public BusinessInsuficientStock(String message) {
        super(message);
    }
}
