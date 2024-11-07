package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.UserEntity;
import com.example.demo.service.PaymentService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @RequestMapping("paymentSelect")
    public String selectPaymentMethod() {
        return "payment/paymentSelect";
    }

    @RequestMapping("creditCardSettings")
    public String creditCardSettings() {
        return "payment/creditCardSettings";
    }

    @RequestMapping("paypalSettings")
    public String paypalSettings() {
        return "payment/paypalSettings";
    }

    @PostMapping("/purchase")
    public String purchase(
            @RequestParam String paymentType,
            @RequestParam(required = false) String cardNumber,
            @RequestParam(required = false) String cardHolder,
            @RequestParam(required = false) String email,
            HttpSession session,
            Model model) {

        UserEntity user = (UserEntity) session.getAttribute("user");
        try {
            paymentService.processPurchase(paymentType, cardNumber, cardHolder, email, user, session);
            return "payment/success";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", "不明な支払い方法が指定されました: " + paymentType);
            return "payment/paymentSelect";
        }
    }
}
