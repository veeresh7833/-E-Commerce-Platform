package com.example.paymentserviceapr24.paymentgateways;

public interface PaymentGateway {

    public String generatePaymentLink(String orderId, Long amount, String email, String phoneNumber);
}
