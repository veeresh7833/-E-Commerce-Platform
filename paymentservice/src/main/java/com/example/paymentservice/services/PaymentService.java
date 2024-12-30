package com.example.paymentservice.services;

import com.example.paymentservice.paymentgateways.PaymentGateway;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    private PaymentGateway paymentGateway;

    public PaymentService(PaymentGateway paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    public String initiatePayment(String orderId, Long amount, String email, String phoneNumber) {
        /*
        Order order = orderService.getOrderDetails(orderId);
        Long amount = 10.10;
         */

        return paymentGateway.generatePaymentLink(orderId, amount, email, phoneNumber);
    }
}
