package com.example.demo;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Demo2Application {

    public static void main(String[] args) {

        ApplicationContext context = SpringApplication.run(Demo2Application.class, args);
        //var orderService = new OrderService(new StripePaymentService());
       var orderService = context.getBean(OrderService.class);
       orderService.placeOrder();

    }

}
