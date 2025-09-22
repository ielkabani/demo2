package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

// @Service
public class OrderService {
    private PaymentService paymentService;

//    @Autowired
    public OrderService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
    public void placeOrder() {
       //var paymentService = new PaypalPaymentService();
         paymentService.processPayment(100.0);
    }
}
