package com.example.paymentservice.controllers;

import com.example.paymentservice.dtos.IntiatePaymentDTO;
import com.example.paymentservice.services.PaymentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    private PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public String initiatePayment(@RequestBody IntiatePaymentDTO intiatePaymentDTO) {
        return paymentService.initiatePayment(intiatePaymentDTO.getOrderId(),
                intiatePaymentDTO.getAmount(),
                intiatePaymentDTO.getEmail(),
                intiatePaymentDTO.getPhoneNumber());
    }

    @PostMapping("/webhook")
    public String listenToWebhook(@RequestBody IntiatePaymentDTO intiatePaymentDTO) {
        return paymentService.initiatePayment(intiatePaymentDTO.getOrderId(),
                intiatePaymentDTO.getAmount(),
                intiatePaymentDTO.getEmail(),
                intiatePaymentDTO.getPhoneNumber());
    }

}
