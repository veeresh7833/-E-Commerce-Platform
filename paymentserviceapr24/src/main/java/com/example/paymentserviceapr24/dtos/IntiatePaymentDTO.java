package com.example.paymentserviceapr24.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IntiatePaymentDTO {
    private String email;
    private String phoneNumber;
    private Long amount;
    private String orderId;
}
