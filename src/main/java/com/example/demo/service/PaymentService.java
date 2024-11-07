package com.example.demo.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.CartEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.service.PaymentMethodClass.CreditCardPayment;
import com.example.demo.service.PaymentMethodClass.PayPalPayment;
import com.example.demo.service.PaymentMethodClass.PaymentMethod;

import jakarta.servlet.http.HttpSession;

@Service
public class PaymentService {

    private final BookPurchaseService bookPurchaseService;

    public PaymentService(BookPurchaseService bookPurchaseService) {
        this.bookPurchaseService = bookPurchaseService;
    }

    @Transactional
    public void processPurchase(String paymentType, String cardNumber, String cardHolder, String email,
                                UserEntity user, HttpSession session) {
        PaymentMethod paymentMethod = createPaymentMethod(paymentType, cardNumber, cardHolder, email);
        List<CartEntity> cartList = createCartList(session, user);
        Integer userId = user != null ? user.getUserId() : cartList.get(0).getUserId();
        
        bookPurchaseService.purchaseBook(cartList, userId, paymentMethod, session);
    }

    private PaymentMethod createPaymentMethod(String paymentType, String cardNumber, String cardHolder, String email) {
        switch (paymentType) {
            case "credit-card":
                return new CreditCardPayment(cardNumber, cardHolder);
            case "paypal":
                return new PayPalPayment(email);
            default:
                throw new IllegalArgumentException("Unknown payment method: " + paymentType);
        }
    }

    private List<CartEntity> createCartList(HttpSession session, UserEntity user) {
        return user != null ? bookPurchaseService.getCart(user.getUserId()) : convertSessionCartToEntityList(session);
    }

    private List<CartEntity> convertSessionCartToEntityList(HttpSession session) {
        @SuppressWarnings("unchecked")
        Map<Integer, Map<String, Object>> cartMap = (Map<Integer, Map<String, Object>>) session.getAttribute("cartList");
        List<CartEntity> cartList = new ArrayList<>();

        for (Map.Entry<Integer, Map<String, Object>> entry : cartMap.entrySet()) {
            Integer bookId = entry.getKey();
            Map<String, Object> cartItem = entry.getValue();
            CartEntity cartEntity = new CartEntity();
            cartEntity.setUserId((Integer) cartItem.get("userId"));
            cartEntity.setBookId(bookId);
            cartEntity.setTitle((String) cartItem.get("title"));
            cartEntity.setAuthorName((String) cartItem.get("authorName"));
            cartEntity.setPrice((BigDecimal) cartItem.get("price"));
            cartEntity.setQuantity((Integer) cartItem.get("quantity"));
            cartList.add(cartEntity);
        }
        return cartList;
    }
}
