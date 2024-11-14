package com.example.demo.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.UserEntity;
import com.example.demo.mapper.BookPurchaseMapper;
import com.example.demo.service.PaymentMethodClass.CreditCardPayment;
import com.example.demo.service.PaymentMethodClass.PayPalPayment;
import com.example.demo.service.PaymentMethodClass.PaymentMethod;

import jakarta.servlet.http.HttpSession;

@Service
public class PaymentService {

	@Autowired
	public CartMethodClass loginCartService;
	@Autowired
	public CartMethodClass guestCartService;
	@Autowired
	public BookOrderService bookOrderService;
	@Autowired
	public BookPurchaseMapper bookPurchaseMapper;

	private PaymentMethod selectPaymentMethod(String paymentType, String cardNumber, String cardHolder, String email) {
		switch (paymentType) {
		case "credit-card":
			return new CreditCardPayment(cardNumber, cardHolder);
		case "paypal":
			return new PayPalPayment(email);
		default:
			throw new IllegalArgumentException("Unknown payment method: " + paymentType);
		}
	}
	
	public void refreshCart(HttpSession session, UserEntity user) {
		bookPurchaseMapper.refreshCart(user.getUserId());	
		session.removeAttribute("cartList");
	}

	@Transactional
    public void processPurchase(String paymentType, String cardNumber, String cardHolder, String email,
                                UserEntity user, HttpSession session) {
		CartMethodClass cartMethodClass = (user != null) ? loginCartService : guestCartService;
		
        Map<Integer, Map<String, Object>> cartList = cartMethodClass.getCart(session, (UserEntity) session.getAttribute("user"));
        
        bookOrderService.orderMethod(cartList, user);
        
        cartMethodClass.refreshCart(session, user);
        
        selectPaymentMethod(paymentType, cardNumber, cardHolder, email);
    }
}
