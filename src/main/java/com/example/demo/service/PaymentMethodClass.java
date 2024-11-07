package com.example.demo.service;

import java.math.BigDecimal;

public class PaymentMethodClass {
	public interface PaymentMethod {
		void pay(BigDecimal amount);
	}

	public static class CreditCardPayment implements PaymentMethod {
		private String cardNumber;
		private String cardHolder;

		public CreditCardPayment(String cardNumber, String cardHolder) {
			this.cardNumber = cardNumber;
			this.cardHolder = cardHolder;
		}
		
		@Override
		public void pay(BigDecimal amount) {
			System.out.println("クレカ払い");
			//実際の処理
		}
	}
	
	public static class PayPalPayment implements PaymentMethod {
		private String email;

		public PayPalPayment(String email) {
            this.email = email;
        }

        @Override
        public void pay(BigDecimal amount) {
            System.out.println("ペイパル決済");
            // 実際の決済処理
        }
    }
	
	//その他の決済方法を記載
	//代引き・銀行振込
}